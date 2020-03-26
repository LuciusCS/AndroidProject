package demo.lucius.blemodule.module.task;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.text.TextUtils;

import java.util.concurrent.Callable;

import demo.lucius.blemodule.log.LogUtils;
import demo.lucius.blemodule.module.service.BleService;
import demo.lucius.blemodule.protocol.down.BleDownMessage;
import demo.lucius.blemodule.protocol.up.BleUp;
import demo.lucius.blemodule.protocol.utils.ProtocolUtil;
import demo.lucius.blemodule.protocol.utils.ble.BleResendThread;
import demo.lucius.blemodule.protocol.utils.ble.BleSendCtrl;
import demo.lucius.blemodule.protocol.utils.ble.BleSendThread;


/***
 * 用于Ble自动自动连接并配置任务
 */
public class BleTaskCallable implements Callable<Boolean> {


    private BluetoothGatt bluetoothGatt;

    private Context context;
    private BluetoothAdapter bluetoothAdapter;
    private String address;

    //用于表示蓝牙连接的状态
    private int bleConnectionState = 0;

    //用于表示蓝牙连接已断开
    private final int STATE_DISCONNECTED = 0;

    //用于表示蓝牙正在进行连接
    private final int STATE_CONNECTING = 1;

    //用于表示蓝牙已建立连接
    private final int STATE_CONNECTED = 2;


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
//                setBleNotification();
                boolean tmp = sendBleData(BleDownMessage.bleprobe());
//                LogUtils.printInfo(tmp + "++++++++++++++++++++++");
                //发送试探帧完成后，启动发送和重发线程
                BleSendCtrl.getInstance().setBluetoothGatt(bluetoothGatt);
                BleSendThread bleSendThread = new BleSendThread();
                BleResendThread bleResendThread = new BleResendThread();
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

    //用于表示构造函数
    public BleTaskCallable(Context context, BluetoothAdapter bluetoothAdapter, String address) {
         this.context=context;
         this.bluetoothAdapter=bluetoothAdapter;
         this.address=address;
    }


    @Override
    public Boolean call() throws Exception {
        return null;
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


        bluetoothGatt = bluetoothDevice.connectGatt(context, false, gattCallback);
        bleConnectionState = STATE_CONNECTED;
        LogUtils.printInfo(bleConnectionState + "");
        return true;
    }

    //用于发送字节小于20字节的报文
    public boolean sendBleData(byte[] data) {
        //获取蓝牙设备的服务
        BluetoothGattService bleGattService = null;
        LogUtils.printInfo("bleGatt：" + bluetoothGatt);
        if (bluetoothGatt != null) {
            bleGattService = bluetoothGatt.getService(BleService.SERVICE_UUID);
        }
        LogUtils.printInfo("bleGattService：" + bleGattService);
        if (bleGattService == null) {
            return false;
        }

        //用于获取蓝牙设备的特征
        BluetoothGattCharacteristic bleGattCharacteristic =
                bleGattService.getCharacteristic(BleService.CHARACTERISTIC_WRITE_UUID);
        LogUtils.printInfo("bleGattCharacteristic：" + bleGattCharacteristic);
        if (bleGattCharacteristic == null) {
            return false;
        }
        bleGattCharacteristic.setValue(data);
        //设置设备回应类型， default 需要设备回应，不需要设备回应
//        bleGattCharacteristic.setWriteType(BluetoothGattCharacteristic.WRITE_TYPE_DEFAULT);


        return bluetoothGatt.writeCharacteristic(bleGattCharacteristic);
    }

}
