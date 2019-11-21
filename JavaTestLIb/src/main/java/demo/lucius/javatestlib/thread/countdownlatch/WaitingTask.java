package demo.lucius.javatestlib.thread.countdownlatch;


import java.util.concurrent.CountDownLatch;

/***
 * 用于等待任务执行结束的线程
 */
public class WaitingTask implements Runnable {

    private final CountDownLatch latch;

    public WaitingTask(CountDownLatch latch) {
        this.latch = latch;
    }

    @Override
    public void run() {
        try {
            latch.await();
            System.out.println("Latch阻塞运行至waiting class");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
