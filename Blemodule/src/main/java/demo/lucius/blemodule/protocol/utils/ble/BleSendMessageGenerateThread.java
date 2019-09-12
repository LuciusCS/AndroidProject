package demo.lucius.blemodule.protocol.utils.ble;

/**
 *
 */
public class BleSendMessageGenerateThread extends Thread {


    int num=1;

    //用于不断添加新的报文
    @Override
    public void run() {
//        super.run();
        while (true){
//            BleSendCtrl.sendQueue.add(num+++"");
            System.out.println("生成报文"+num);
            try {
                Thread.sleep(500);
//                if (num%3==1){
//                    BleSendCtrl.resendTimer=0;
//                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
