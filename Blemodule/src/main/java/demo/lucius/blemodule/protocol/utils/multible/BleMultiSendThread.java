package demo.lucius.blemodule.protocol.utils.multible;

/***
 * 用于蓝牙报文发送线程
 */
public class BleMultiSendThread extends Thread{

    private BleMultiSendCtrl bleMultiSendCtrl;

    public BleMultiSendThread(BleMultiSendCtrl bleMultiSendCtrl) {
        this.bleMultiSendCtrl = bleMultiSendCtrl;
//        bleMultiSendCtrl.setBleMultiSendThread(this);
    }

    public BleMultiSendThread() {
    }

    @Override
    public void run() {
//        super.run();
//        this.bleMultiSendCtrl =BleMultiSendCtrl.getInstance();
        while (true) {
            System.out.println("send+++++++++++++++++++++++++++++++++++");
            bleMultiSendCtrl.send();
        }
    }
}
