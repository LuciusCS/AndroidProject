package demo.lucius.blemodule.moudle.service;


import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothProfile;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.text.TextUtils;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import demo.lucius.blemodule.log.LogUtils;
import demo.lucius.blemodule.protocol.down.BleDownMessage;
import demo.lucius.blemodule.protocol.up.BleUp;
import demo.lucius.blemodule.protocol.utils.ProtocolUtil;
import demo.lucius.blemodule.protocol.utils.ble.BleResendThread;
import demo.lucius.blemodule.protocol.utils.ble.BleSendCtrl;
import demo.lucius.blemodule.protocol.utils.ble.BleSendThread;

/**
 * 用于处理蓝牙自动搜索
 */
public class BleService extends Service {

//    LinkedTransferQueue

    private final String TAG = BleService.class.getSimpleName();

    private BluetoothGatt bluetoothGatt;

    //用于表示蓝牙连接的状态
    private int bleConnectionState = 0;

    //用于表示蓝牙连接已断开
    private final int STATE_DISCONNECTED = 0;

    //用于表示蓝牙正在进行连接
    private final int STATE_CONNECTING = 1;

    //用于表示蓝牙已建立连接
    private final int STATE_CONNECTED = 2;

    //用于表示蓝牙已连接
    public final static String ACTION_GATT_CONNECTED = "ACTION_GATT_CONNECTED";
    //用于表示蓝牙已断开连接
    public final static String ACTION_GATT_DISCONNECTED = "ACTION_GATT_DISCONNECTED";
    //用于表示发现蓝牙服务
    public final static String ACTION_GATT_SERVICE_DISCOVERED = "ACTION_GATT_DISCOVERED";
    //用于表示收到蓝牙的数据
    public final static String ACTION_DATA_AVAILABLE = "ACTION_DATA_AVAILABLE";
    //用于表示蓝牙连接失败
    public final static String ACTION_DATA_CONNECT_FAIL = "ACTION_CONNECT_FAIL";

    //用于表示蓝牙数据
    public final static String EXTRA_DATA = "BLE_EXTRA";

    //用于表示服务标识
    public final static UUID SERVICE_UUID = UUID.fromString("6e400001-b5a3-f393-e0a9-e50e24dcca9e");

    //特征标识（发送数据）
    public final static UUID CHARACTERISTIC_WRITE_UUID = UUID.fromString("6e400002-b5a3-f393-e0a9-e50e24dcca9e");
    //特征标识（读取数据）
    private final UUID CHARACTERISTIC_READ_UUID = UUID.fromString("6e400003-b5a3-f393-e0a9-e50e24dcca9e");
//    //用于表示通知的UUID
//    private final UUID CHARACTERISTIC_NOTIFY = UUID.fromString("6e400003-b5a3-f393-e0a9-e50e24dcca9e");

    //用于表示描述UUID
    private final UUID DESCRPTOR_UUID = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb");


    //用于绑定服务
    private final IBinder mbinder = new LocalBinder();


    public class LocalBinder extends Binder {

        public BleService getService() {
            return BleService.this;
        }

    }

    //用于蓝牙操作的回调,蓝牙处于连接状态才会进行回调
    private final BluetoothGattCallback gattCallback = new BluetoothGattCallback() {

        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
//            super.onConnectionStateChange(gatt, status, newState);
            //连接状态发生变化
            switch (newState) {

                case BluetoothProfile.STATE_CONNECTED:
                    //蓝牙已连接
                    bleConnectionState = STATE_CONNECTED;
                    //搜索GATT服务
                    bluetoothGatt.discoverServices();
                    LogUtils.printInfo("蓝牙建立连接");
                    break;
                case BluetoothProfile.STATE_DISCONNECTED:
                    //用于表示蓝牙已断开连接
                    bleConnectionState = STATE_DISCONNECTED;
                    LogUtils.printInfo("蓝牙断开连接");
                    break;
            }

        }

        @Override
        public void onMtuChanged(BluetoothGatt gatt, int mtu, int status) {
            super.onMtuChanged(gatt, mtu, status);
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
//            super.onServicesDiscovered(gatt, status);
            //用于表示GATT发现的服务
            if (status == BluetoothGatt.GATT_SUCCESS) {
                setBleNotification();
                boolean tmp = sendBleData(BleDownMessage.bleprobe());
//                LogUtils.printInfo(tmp + "++++++++++++++++++++++");
                //发送试探帧完成后，启动发送和重发线程
                BleSendCtrl.getInstance().setBluetoothGatt(bluetoothGatt);
                BleSendThread bleSendThread=new BleSendThread();
                BleResendThread bleResendThread=new BleResendThread();
                bleSendThread.start();
                bleResendThread.start();

            }
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            super.onCharacteristicRead(gatt, characteristic, status);
            //读取特征后回调
            if (status == BluetoothGatt.GATT_SUCCESS) {
                //表示程序中的特征值已经读取完成，特征所包含的值在一个数组中
                byte[] characteristicValueByte = characteristic.getValue();
                BleUp bleUp = BleUp.getInstance();
                bleUp.setMessage(characteristicValueByte);

            }
        }

        //执行gatt.setCharacteristicNotification，结果会回调在此
        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            super.onCharacteristicChanged(gatt, characteristic);
            //当特征值发生变化时回调
//            sendBleBroadcast(ACTION_DATA_AVAILABLE, characteristic);

            //当我们决定用通知的方式获取外设特征值的时候，每当特征值发生变化，程序就会回调到此处。
            //在一个gatt链接当中，可以同时存在多个notify的回调，全部值都会回调到这里，就需要去判断回调回来的
            // 特征的UUID，因为UUID是唯一的，所以我们可以用UUID来确定，这些数据来自哪个特征。
            //假设我们已经在BleService当中定了多个我们想要使用的静态UUID，前面已经说过如何表达一个UUID
            //那么我们需要做的就是对比这些UUID，根据不同的UUID来分类这些数据，究竟应该交由哪个方法来处理
            //所以，这么一来我们便会发现其实上面的onCharacteristicRead也会出现这种情况，
            //因为我们不可能只读取一个特征，除非这个外设也只有这一个特征，
            //究竟是谁在读取，读取的值来自于哪个特征等，都需要进行判断。
//            LogUtils.printInfo(characteristic.getUuid().toString());
            LogUtils.printInfo(characteristic.getService().getUuid().toString());
            LogUtils.printInfo(characteristic.getUuid().toString());
//            if (characteristic.getUuid())
            byte[] characteristicValueByte = characteristic.getValue();
            LogUtils.printInfo(ProtocolUtil.byteToHexStringSpace(characteristicValueByte));
            BleUp bleUp = BleUp.getInstance();
            bleUp.setMessage(characteristicValueByte);
            switch (characteristic.getUuid().toString()) {
                default:
                    break;
            }

        }

        //执行写入特征的时候会回调此函数
        @Override
        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            super.onCharacteristicWrite(gatt, characteristic, status);
            LogUtils.printInfo(gatt + "++++" + characteristic + "++++" + status);
        }

        @Override
        public void onDescriptorRead(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
            super.onDescriptorRead(gatt, descriptor, status);
            //读取描述符回调
        }


        @Override
        public void onReadRemoteRssi(BluetoothGatt gatt, int rssi, int status) {
            super.onReadRemoteRssi(gatt, rssi, status);
            //rssi表示设备与中心信号强度，发生变化回调
        }


    };


    @Override
    public IBinder onBind(Intent intent) {
        return mbinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {

        return super.onUnbind(intent);
    }


    //用于蓝牙建立连接
    public boolean connect(BluetoothAdapter bluetoothAdapter, String address) {
        if (bluetoothAdapter == null || TextUtils.isEmpty(address)) {
            LogUtils.printInfo(false + "");
            return false;
        }
        BluetoothDevice bluetoothDevice = bluetoothAdapter.getRemoteDevice(address);
        if (bluetoothDevice == null) {
            LogUtils.printInfo(false + "");
            return false;
        }


        bluetoothGatt = bluetoothDevice.connectGatt(this, false, gattCallback);
        bleConnectionState = STATE_CONNECTED;
        LogUtils.printInfo(bleConnectionState + "");
        return true;
    }


    //用于蓝牙断开连接
    public void disconnected() {
        if (bluetoothGatt == null) {
            return;
        }
        bluetoothGatt.disconnect();
    }

    //用于释放相关资源
    public void release() {
        if (bluetoothGatt != null) {
            return;
        }
        bluetoothGatt.close();
        bluetoothGatt = null;
    }

    //设置蓝牙设备在数据改变时，通知APP
    public void setBleNotification() {
        if (bluetoothGatt == null) {
            return;
        }

        //用于获取蓝牙设备的服务
        BluetoothGattService bluetoothGattService = bluetoothGatt.getService(SERVICE_UUID);
        if (bluetoothGattService == null) {
            return;
        }


        //用于获取蓝牙设备的特征
        BluetoothGattCharacteristic bluetoothGattCharacteristic =
                bluetoothGattService.getCharacteristic(CHARACTERISTIC_READ_UUID);
        if (bluetoothGattService == null) {
//            sendBleBroadcast(ACTION_DATA_CONNECT_FAIL);
            return;
        }

        //用于获取蓝牙设备特征的描述符
        BluetoothGattDescriptor bluetoothGattDescriptor = bluetoothGattCharacteristic.getDescriptor(DESCRPTOR_UUID);
        bluetoothGattDescriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
        if (bluetoothGatt.writeDescriptor(bluetoothGattDescriptor)) {
            //蓝牙设备在数据改变时，通知APP,app在收到数据后回调onCharacteristicChanged方法
            bluetoothGatt.setCharacteristicNotification(bluetoothGattCharacteristic, true);
        }
    }



    /*在官方的写法中，采用了Broadcast的方式来让Service向其他组件发送蓝牙回调数据，
  这么做当然是可以的，但是如果一个蓝牙设备包含了很多个服务而且服务当中存在大量
  Notify方式读取的数据，那么Service收到这些数据后，又用Broadcast的方式广播数据，
  系统中就会存在大量广播而导致app性能下降，如何解决这种问题，我们可以使用回调
  的方式的方式来触发外部activity或fragment等组件取得数据更新UI或其他操作，例如我们
  可以这么写：
*/
/*我们先定义一个回调接口，让外部Activity或Fragment实现，又或直接新建一个类实现。
  我们在Activity绑定Service后获得Binder对象时，把实现了ICallback的回调接口类通过
  setter方式传入到Service当中作为Service的成员变量，例如可在Service的内部Binder类
  写一个方法setFragments或setActivities之类的。至于回调的工作方式，这里不做详尽介绍了。
*/


    //用于发送字节小于20字节的报文
    public boolean sendBleData(byte[] data) {
        //获取蓝牙设备的服务
        BluetoothGattService bleGattService = null;
        LogUtils.printInfo("bleGatt：" + bluetoothGatt);
        if (bluetoothGatt != null) {
            bleGattService = bluetoothGatt.getService(SERVICE_UUID);
        }
        LogUtils.printInfo("bleGattService：" + bleGattService);
        if (bleGattService == null) {
            return false;
        }

        //用于获取蓝牙设备的特征
        BluetoothGattCharacteristic bleGattCharacteristic = bleGattService.getCharacteristic(CHARACTERISTIC_WRITE_UUID);
        LogUtils.printInfo("bleGattCharacteristic：" + bleGattCharacteristic);
        if (bleGattCharacteristic == null) {
            return false;
        }
        bleGattCharacteristic.setValue(data);
        //设置设备回应类型， default 需要设备回应，不需要设备回应
//        bleGattCharacteristic.setWriteType(BluetoothGattCharacteristic.WRITE_TYPE_DEFAULT);


        return bluetoothGatt.writeCharacteristic(bleGattCharacteristic);
    }


    //用于判断特征是否可写
    private boolean ifCharacteristicWritable(BluetoothGattCharacteristic bluetoothGattCharacteristic) {
        return ((bluetoothGattCharacteristic.getProperties() & BluetoothGattCharacteristic.PROPERTY_WRITE) > 0 ||
                (bluetoothGattCharacteristic.getProperties() & BluetoothGattCharacteristic.WRITE_TYPE_NO_RESPONSE) > 0);
    }

    //用于判断特征是否可读
    private boolean ifCharacteristicReadable(BluetoothGattCharacteristic bluetoothGattCharacteristic) {
        return ((bluetoothGattCharacteristic.getProperties() & BluetoothGattCharacteristic.PROPERTY_READ) > 0);
    }

    //用于判断是否具有通知属性
    private boolean ifCharacteristicNotifiable(BluetoothGattCharacteristic bluetoothGattCharacteristic) {
        return ((bluetoothGattCharacteristic.getProperties() & BluetoothGattCharacteristic.PROPERTY_NOTIFY) > 0 ||
                (bluetoothGattCharacteristic.getProperties() & BluetoothGattCharacteristic.PROPERTY_INDICATE) > 0);
    }

    //用于读取特征，会回调gattCallback
    private void readCharacteristic(BluetoothGattCharacteristic bluetoothGattCharacteristic) {
        bluetoothGatt.readCharacteristic(bluetoothGattCharacteristic);
    }

    //用于写入特征，会回调gattCallback
    private void writeCharacteristic(BluetoothGattCharacteristic bluetoothGattCharacteristic) {
        bluetoothGatt.writeCharacteristic(bluetoothGattCharacteristic);
    }

    //用于设置通知，会回调gattCallback
    private void setCharacteristicNotification(BluetoothGattCharacteristic bluetoothGattCharacteristic,
                                               boolean enable) {
        bluetoothGatt.setCharacteristicNotification(bluetoothGattCharacteristic, enable);

//        //在明确知道当前特征的描述符前提下，可以直接使用描述符，不需要做判断，
//        //但如果不知道此特征是否具有描述符的情况下，没有以下几行代码可能会导致设置通知失败的情况发生。
//        List<BluetoothGattDescriptor> descriptorList = characteristic.getDescriptors();
//        if (descriptorList != null) {
//            for (BluetoothGattDescriptor descriptor : descriptorList) {
//                byte[] value = enable ? BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE : BluetoothGattDescriptor
//                .DISABLE_NOTIFICATION_VALUE;
//                descriptor.setValue(value);
//                mBluetoothGatt.writeDescriptor(descriptor);
//            }
//        }
    }


}
