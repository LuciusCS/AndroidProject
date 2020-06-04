package demo.lucius.blemodule.protocol.utils.ble;

public class BleMain {


    public static void main(String[] args) throws Exception {

         BleSendMessageGenerateThread bleSendMessageGenerateThread=new BleSendMessageGenerateThread();
         BleSendThread bleSendThread=new BleSendThread();
         BleResendThread bleResendThread=new BleResendThread();


         bleSendMessageGenerateThread.start();
         bleSendThread.start();
         bleResendThread.start();

    }
}
