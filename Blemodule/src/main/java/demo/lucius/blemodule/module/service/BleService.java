package demo.lucius.blemodule.module.service;


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

import java.util.UUID;

import demo.lucius.blemodule.log.LogUtils;
import demo.lucius.blemodule.module.observe.ProtocolObservable;
import demo.lucius.blemodule.protocol.down.BleDownMessage;
import demo.lucius.blemodule.protocol.up.BleUp;
import demo.lucius.blemodule.protocol.utils.ProtocolUtil;
import demo.lucius.blemodule.protocol.utils.ble.BleResendThread;
import demo.lucius.blemodule.protocol.utils.ble.BleSendCtrl;
import demo.lucius.blemodule.protocol.utils.ble.BleSendThread;
import demo.lucius.blemodule.protocol.utils.ble.SendMessageObject;
import demo.lucius.blemodule.utils.databus.LiveDataBus;

import static android.bluetooth.BluetoothGatt.CONNECTION_PRIORITY_LOW_POWER;

/**
 * 用于处理蓝牙自动搜索
 */
public class BleService extends Service {

//    LinkedTransferQueue

    private final String TAG = BleService.class.getSimpleName();

    private BluetoothGatt bluetoothGatt;

    //用于表示蓝牙连接的状态
    private int bleConnectionState = 0;


    //用于表示蓝牙正在进行连接
    private final int STATE_CONNECTING = 1;


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
    private final static UUID CHARACTERISTIC_READ_UUID = UUID.fromString("6e400003-b5a3-f393-e0a9-e50e24dcca9e");
//    //用于表示通知的UUID
//    private final UUID CHARACTERISTIC_NOTIFY = UUID.fromString("6e400003-b5a3-f393-e0a9-e50e24dcca9e");

    //用于表示描述UUID
    private final static UUID DESCRPTOR_UUID = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb");


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
                    bleConnectionState = BluetoothProfile.STATE_CONNECTED;
                    //搜索GATT服务
                    bluetoothGatt.discoverServices();
                    ProtocolObservable.getInstance().notifyObservers("ble_connected");
                    LiveDataBus.get().with("ble_key").postValue(String.valueOf("ble_connected"));
                    LogUtils.printInfo("蓝牙已连接");
                    break;
                case BluetoothProfile.STATE_DISCONNECTED:
                    //用于表示蓝牙已断开连接
                    bleConnectionState = BluetoothProfile.STATE_DISCONNECTED;
                    ProtocolObservable.getInstance().notifyObservers("ble_disconnected");
                    LiveDataBus.get().with("ble_key").postValue(String.valueOf("ble_disconnected"));
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

            LogUtils.printInfo(gatt.getServices().size()+"+++++++++++++++");
            LogUtils.printInfo(status+"+++++++++++++++");

            //用于表示GATT发现的服务
            if (status == BluetoothGatt.GATT_SUCCESS) {
               if (setBleNotification()) {
                   //在这里直接发送试探帧失败，要进行一些初始化操作
//                boolean tmp1 = sendBleData(BleDownMessage.bleprobe());
//                LogUtils.printInfo(tmp1 + "发送结果++++++++++++++++++++++");
//                try {
//                    Thread.sleep(2000);
//                    boolean tmp = sendBleData(BleDownMessage.bleprobe());
//                    LogUtils.printInfo(tmp + "发送结果++++++++++++++++++++++");
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
                   //发送试探帧完成后，启动发送和重发线程
                   BleSendCtrl.getInstance().setBluetoothGatt(bluetoothGatt);
                   BleSendThread bleSendThread = new BleSendThread();
                   BleResendThread bleResendThread = new BleResendThread();
                   bleSendThread.start();
                   bleResendThread.start();

                   //用于发送试探帧
                   SendMessageObject sendMessageObject1 = new SendMessageObject.Builder(SendMessageObject.BLE_DIRECT_SEND,
                           BleDownMessage.bleprobe()).build();
                   try {
                       Thread.sleep(1000);
                       BleSendCtrl.sendQueue.put(sendMessageObject1);
                       ProtocolObservable.getInstance().notifyObservers("ble_prob");
                       LiveDataBus.get().with("ble_key").postValue(String.valueOf("ble_prob"));
                   } catch (InterruptedException e) {
                       e.printStackTrace();
                   }
               }else {
//                   connect();
                   LogUtils.printInfo("连接失败");
               }

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
        bluetoothGatt.requestConnectionPriority(CONNECTION_PRIORITY_LOW_POWER);
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
    public boolean setBleNotification() {
        if (bluetoothGatt == null) {
            return false;
        }

        //用于获取蓝牙设备的服务
        BluetoothGattService bluetoothGattService = bluetoothGatt.getService(SERVICE_UUID);
        if (bluetoothGattService == null) {
            return false;
        }


        //用于获取蓝牙设备的特征
        BluetoothGattCharacteristic bluetoothGattCharacteristic =
                bluetoothGattService.getCharacteristic(CHARACTERISTIC_READ_UUID);
        if (bluetoothGattService == null) {
//            sendBleBroadcast(ACTION_DATA_CONNECT_FAIL);
            return false;
        }

        //用于获取蓝牙设备特征的描述符
        BluetoothGattDescriptor bluetoothGattDescriptor = bluetoothGattCharacteristic.getDescriptor(DESCRPTOR_UUID);
        bluetoothGattDescriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
        if (bluetoothGatt.writeDescriptor(bluetoothGattDescriptor)) {
            //蓝牙设备在数据改变时，通知APP,app在收到数据后回调onCharacteristicChanged方法
           return bluetoothGatt.setCharacteristicNotification(bluetoothGattCharacteristic, true);
        }

        return false;
    }



    //用于发送字节小于20字节的报文
//    public boolean sendBleData(byte[] data) {
//        //获取蓝牙设备的服务
//        BluetoothGattService bleGattService = null;
//        LogUtils.printInfo("bleGatt：" + bluetoothGatt);
//        if (bluetoothGatt != null) {
//            bleGattService = bluetoothGatt.getService(SERVICE_UUID);
//        }
//        LogUtils.printInfo("bleGattService：" + bleGattService);
//        if (bleGattService == null) {
//            return false;
//        }
//
//        //用于获取蓝牙设备的特征
//        BluetoothGattCharacteristic bleGattCharacteristic = bleGattService.getCharacteristic(CHARACTERISTIC_WRITE_UUID);
//        LogUtils.printInfo("bleGattCharacteristic：" + bleGattCharacteristic);
//        if (bleGattCharacteristic == null) {
//            return false;
//        }
//        bleGattCharacteristic.setValue(data);
//        //设置设备回应类型， default 需要设备回应，不需要设备回应
////        bleGattCharacteristic.setWriteType(BluetoothGattCharacteristic.WRITE_TYPE_DEFAULT);
//
//        boolean tmp= bluetoothGatt.writeCharacteristic(bleGattCharacteristic);
//        LogUtils.printInfo(tmp+"+++++++++++++++++");
//        return tmp;
//    }


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
