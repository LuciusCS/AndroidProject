package demo.lucius.blemodule.protocol.utils.multible;


import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BleMultiMain {

    final static int SIZE = 1;

    public static void main(String[] args) throws Exception {

        ExecutorService exec = Executors.newFixedThreadPool(10);
        CountDownLatch latch = new CountDownLatch(SIZE);

        BleMultiSendCtrl bleMultiSendCtrl = new BleMultiSendCtrl(latch);


        BleSendMessageMultiThread bleSendMessageGenerateThread = new BleSendMessageMultiThread(bleMultiSendCtrl);
        BleMultiSendThread bleSendThread = new BleMultiSendThread(bleMultiSendCtrl);
        BleMultiResendThread bleResendThread = new BleMultiResendThread(bleMultiSendCtrl);

        exec.execute(  bleSendMessageGenerateThread);
        bleSendThread.start();
        bleResendThread.start();


        exec.execute(new WaitingBleFinishTask(latch));
        //启动门栓，启动等待线程
        exec.execute(bleMultiSendCtrl);

        exec.shutdown();

//        BleMultiSendCtrl bleMultiSendCtrl1=new BleMultiSendCtrl();
//        BleSendMessageMultiThread bleSendMessageGenerateThread1 = new BleSendMessageMultiThread(bleMultiSendCtrl1);
//        BleMultiSendThread bleSendThread1 = new BleMultiSendThread(bleMultiSendCtrl1);
//        BleMultiResendThread bleResendThread1 = new BleMultiResendThread(bleMultiSendCtrl1);
//
//        bleSendMessageGenerateThread1.start();
//        bleSendThread1.start();
//        bleResendThread1.start();


    }
}
