package demo.lucius.blemodule.protocol.utils.multible;


import java.util.concurrent.CountDownLatch;

/**
 * 用于等待BLE任务结束的线程
 */
public class WaitingBleFinishTask implements Runnable{


    private static int counter = 0;

    private final int id = counter++;

    private final CountDownLatch latch;

    public WaitingBleFinishTask(CountDownLatch latch) {
        this.latch = latch;
    }


    @Override
    public void run() {
        try {
//            System.out.println("Latch阻塞运行至" + this);
            latch.await();
            System.out.println("Latch阻塞运行至" + this);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return String.format("WaitingTask %1$-3d ", id);
    }


}
