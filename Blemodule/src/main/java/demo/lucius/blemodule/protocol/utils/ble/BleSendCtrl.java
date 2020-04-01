package demo.lucius.blemodule.protocol.utils.ble;


import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;

import androidx.lifecycle.Observer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import demo.lucius.blemodule.log.LogUtils;
import demo.lucius.blemodule.module.service.BleService;
import demo.lucius.blemodule.protocol.down.BleDownMessage;
import demo.lucius.blemodule.protocol.utils.ProtocolUtil;
import demo.lucius.blemodule.utils.databus.LiveDataBus;

import static demo.lucius.blemodule.protocol.utils.ble.SendMessageObject.BLE_78_RESEND;
import static demo.lucius.blemodule.protocol.utils.ble.SendMessageObject.BLE_78_SEND;
import static demo.lucius.blemodule.protocol.utils.ble.SendMessageObject.BLE_DIRECT_RESEND;
import static demo.lucius.blemodule.protocol.utils.ble.SendMessageObject.BLE_DIRECT_SEND;


/**
 * 用于BLe接收线程控制
 */
public class BleSendCtrl {

    //用于表示发送队列
    public static BlockingQueue<SendMessageObject> sendQueue = new LinkedBlockingQueue<>();

    //用于表示重发队列
    public static BlockingQueue<SendMessageObject> resendQueue = new LinkedBlockingQueue<>();

    //用于表示重发队列控制
    public static BlockingQueue<String> resendInfo = new LinkedBlockingQueue<>();


    private Lock lock = new ReentrantLock();
    //Ble发送线程控制
    private Condition bleSendCondition = lock.newCondition();
    //Ble重发线程控制
    private Condition bleResendCondition = lock.newCondition();

    //用于表示发送还是重发
    private boolean sendResend = false;

    //用于单例模式表示
    private static BleSendCtrl uniqueInstance = new BleSendCtrl();

    //用于重发计时
    public static volatile int resendTimer = 5;   //resendTimer=-1为取消重新发送，是否需要重发，重发秒数

    private BluetoothGatt bluetoothGatt;

    private BleSendCtrl() {

    }

    public static BleSendCtrl getInstance() {

        LiveDataBus.get().with("ble_key", Integer.class)
                .observeForever(new Observer<Integer>() {
                    @Override
                    public void onChanged(Integer integer) {

                    }
                });
        return uniqueInstance;

    }

    //用于进行发送
    public void send() {

        try {
            lock.lock();
            while (sendResend) {
                LogUtils.printInfo(("发送线程：" + Thread.currentThread().getName() + "await"));
//                conditionCustomer.await();
                bleSendCondition.await();
            }

            //使用take如果队列为空则阻塞
            LogUtils.printInfo(("发送线程：" + Thread.currentThread().getName() + "signal") + "发送队列大小：" + sendQueue.size());
            SendMessageObject sendMessageObject = sendQueue.take();
            LogUtils.printInfo(("发送报文" + ProtocolUtil.byteToHexStringSpace(sendMessageObject.getMessage()) + "队列大小" + sendQueue.size()));
            senMessageCtrl(sendMessageObject);

            //如果该条报文需要重发
            if (sendMessageObject.getType() == BLE_DIRECT_RESEND || sendMessageObject.getType() == BLE_78_RESEND) {
                //用于发送重发次数
                for (int i = sendMessageObject.getResendTime() - 1; i >= 0; i--) {
                    resendQueue.put(sendMessageObject);
                }
                //设置重发时间
                resendTimer = sendMessageObject.getResendTimer();
            }
            LogUtils.printInfo("resendTimer：" + resendTimer);
            sendResend = true;
            bleResendCondition.signal();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }

    }

    //用于进行重发
    public void resend() {
        try {
            lock.lock();
            while (!sendResend) {
                LogUtils.printInfo("重新发送线程：" + Thread.currentThread().getName() + "await");
                bleResendCondition.await();
            }
//            while (--resendTimer > 0) {
//                if (resendInfo.size() != 0) {
//                    resendQueue.clear();
//                    resendInfo.clear();
//                    break;
//                }
//                Thread.sleep(1000);
//            }
//            //使用remove如果为空则抛出异常
//            if (resendQueue.size() > 0 && resendTimer == 0) {
//                SendMessageObject sendMessageObject = resendQueue.take();
//                LogUtils.printInfo("重新发送报文" + ProtocolUtil.byteToHexStringSpace(sendMessageObject.getMessage()) +
//                        "队列大小" + resendQueue.size());
//                senMessageCtrl(sendMessageObject);
//            }

            //测试多次重发
            int tmpNum = resendQueue.size();
            for (int i = 0; i < tmpNum; i++) {
                SendMessageObject sendMessageObject = resendQueue.take();
                LogUtils.printInfo("重新发送报文" + ProtocolUtil.byteToHexStringSpace(sendMessageObject.getMessage()) +
                        "队列大小" + resendQueue.size());
                while (--resendTimer > 0) {
                    if (resendInfo.size() != 0) {
                        break;
                    }
                    LogUtils.printInfo("重发时间：" + resendTimer);
                    Thread.sleep(1000);
                }
                if (resendTimer == 0) {
                    senMessageCtrl(sendMessageObject);
                    resendTimer = sendMessageObject.getResendTimer();
                } else {
                    resendQueue.clear();
                    resendInfo.clear();
                    break;
                }
            }

            sendResend = false;
            bleSendCondition.signal();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        } finally {
            lock.unlock();
        }
    }

    //用于报文发送控制
    private void senMessageCtrl(SendMessageObject sendMessageObj) {
        switch (sendMessageObj.getType()) {
            case BLE_DIRECT_SEND:
                sendBleData(sendMessageObj.getMessage());
                break;
            case BLE_DIRECT_RESEND:
                sendBleData(sendMessageObj.getMessage());
                break;
            case BLE_78_SEND:
                sendLong78Data(sendMessageObj.getMessage());
                break;
            case BLE_78_RESEND:
                sendLong78Data(sendMessageObj.getMessage());
                break;
            default:
                break;
        }
    }

    //用于长报文发送
    private void sendLong78Data(byte[] data) {
        //用于透传78进行分包
        int packageNum = data.length / 16;
        packageNum += data.length % 16 == 0 ? 0 : 1;
        List<byte[]> blePackage = new ArrayList<>();
        for (int i = 0; i < packageNum; i++) {
            byte[] payload = new byte[16];
            if (i == packageNum - 1) {   //最后一帧
                System.arraycopy(data, i * 16, payload, 0, data.length - i * 16);
                sendBleData(BleDownMessage.bleTrans78Data(i, 1, payload));
            } else {
                System.arraycopy(data, i * 16, payload, 0, 16);
                sendBleData(BleDownMessage.bleTrans78Data(i, 0, payload));
            }
        }
//        return blePackage;
    }


    //用于发送20字节BLE报文
    private boolean sendBleData(byte[] data) {
        //获取蓝牙设备的服务
        BluetoothGattService bleGattService = null;
//        LogUtils.printInfo("bleGatt：" + bluetoothGatt);
        if (bluetoothGatt != null) {
            bleGattService = bluetoothGatt.getService(BleService.SERVICE_UUID);
        }
//        LogUtils.printInfo("bleGattService：" + bleGattService);
        if (bleGattService == null) {
            return false;
        }

        //用于获取蓝牙设备的特征
        BluetoothGattCharacteristic bleGattCharacteristic =
                bleGattService.getCharacteristic(BleService.CHARACTERISTIC_WRITE_UUID);
        if (bleGattCharacteristic == null) {
            return false;
        }
        bleGattCharacteristic.setValue(data);
        //设置设备回应类型， default 需要设备回应，不需要设备回应
//        bleGattCharacteristic.setWriteType(BluetoothGattCharacteristic.WRITE_TYPE_DEFAULT);

        //自动重发三次
        boolean sendTmp = false;
        for (int i = 0; i < 3; i++) {
            sendTmp = bluetoothGatt.writeCharacteristic(bleGattCharacteristic);
            if (sendTmp) {
                try {
                    //Ble报文收发自动设置是按间隔
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
        LogUtils.printInfo("报文发送结果"+sendTmp);
        return sendTmp;
    }

    public void setBluetoothGatt(BluetoothGatt bluetoothGatt) {
        this.bluetoothGatt = bluetoothGatt;
    }

    public void addSendMessage(SendMessageObject sendMessageObject){
        sendQueue.add(sendMessageObject);
    }

}
