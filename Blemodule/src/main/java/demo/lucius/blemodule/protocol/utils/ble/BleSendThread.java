package demo.lucius.blemodule.protocol.utils.ble;

/***
 * 用于蓝牙报文发送线程
 */
public class BleSendThread extends Thread{

    private BleSendCtrl bleSendCtrl;

    public BleSendThread(){
        bleSendCtrl=BleSendCtrl.getInstance();
    }

    @Override
    public void run() {
//        super.run();
         while (true){

             bleSendCtrl.send();
         }
    }
}
