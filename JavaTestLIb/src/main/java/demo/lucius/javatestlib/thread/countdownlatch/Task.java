package demo.lucius.javatestlib.thread.countdownlatch;

import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * 用于多个任务同时执行线程
 */
public class Task extends Thread {

    private static Random random = new Random(100);

    private final CountDownLatch latch;

    //使用CountDownLatch会被自动要求加上此构造函数
    public Task(CountDownLatch latch, String name) {
        this.latch = latch;
        this.setName(name);
    }

    @Override
    public void run() {

        try {
            doWork();
            //用于倒数技术
            latch.countDown();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void doWork() throws InterruptedException {
        //random.nextInt(2000)在Java中是线程安全的来自TIJ
        TimeUnit.MILLISECONDS.sleep(random.nextInt(1000));
        System.out.println(this.getName() + "completed");
    }
}
