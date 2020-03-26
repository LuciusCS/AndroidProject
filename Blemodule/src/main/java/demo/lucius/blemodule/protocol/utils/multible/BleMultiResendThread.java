package demo.lucius.blemodule.protocol.utils.multible;

/**
 * 用于蓝牙收发重发
 */
public class BleMultiResendThread extends Thread{

    private BleMultiSendCtrl bleMultiSendCtrl;

    public BleMultiResendThread(BleMultiSendCtrl bleMultiSendCtrl){
        this.bleMultiSendCtrl= bleMultiSendCtrl;
//        bleMultiSendCtrl.setBleMultiResendThread(this);
    }


    public BleMultiResendThread(){
    }

    @Override
    public void run() {
//        super.run();
        while (true){
            System.out.println("resend+++++++++++++++++++++++++++++++++++");
            bleMultiSendCtrl.resend();
        }
    }
}
