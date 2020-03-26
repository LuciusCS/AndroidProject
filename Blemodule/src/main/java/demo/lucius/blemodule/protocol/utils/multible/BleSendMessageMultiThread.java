package demo.lucius.blemodule.protocol.utils.multible;

/**
 *
 */
public class BleSendMessageMultiThread implements Runnable {


    int num = 1;

    private BleMultiSendCtrl bleMultiSendCtrl;

    public BleSendMessageMultiThread(BleMultiSendCtrl bleMultiSendCtrl) {
        this.bleMultiSendCtrl = bleMultiSendCtrl;
    }

//    public BleSendMessageMultiThread() {
//        this.bleMultiSendCtrl =BleMultiSendCtrl.getInstance();
//    }


    //用于不断添加新的报文
    @Override
    public void run() {
//        super.run();
        while (true) {
            bleMultiSendCtrl.addSendMessage("++++" + num++);
            System.out.println("生成报文" + num);
            try {
                Thread.sleep(1000);
//                if (num%3==1){
//                    BleMultiSendCtrl.resendTimer=0;
//                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
