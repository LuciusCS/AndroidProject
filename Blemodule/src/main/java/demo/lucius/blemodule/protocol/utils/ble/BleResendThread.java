package demo.lucius.blemodule.protocol.utils.ble;

/**
 * 用于蓝牙收发重发
 */
public class BleResendThread extends Thread{

    private BleSendCtrl bleSendCtrl;

    public BleResendThread(){
        bleSendCtrl=BleSendCtrl.getInstance();
    }

    @Override
    public void run() {
//        super.run();
        while (true){
            bleSendCtrl.resend();
        }
    }
}
