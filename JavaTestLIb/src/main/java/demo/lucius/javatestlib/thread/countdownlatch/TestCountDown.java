package demo.lucius.javatestlib.thread.countdownlatch;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TestCountDown {

    static final int SIZE = 5;

    public static void main(String[] args) throws Exception {

        ExecutorService exec = Executors.newFixedThreadPool(5);
        //所有需要同时结束的线程，需要使用同一个CountDownLatch对象
        CountDownLatch latch = new CountDownLatch(SIZE);
//        for (int i=0;i<10;i++){   //可以有多个等待线程
        exec.execute(new WaitingTask(latch));
//        }
        for (int i = 0; i < SIZE; i++) {
            exec.execute(new Task(latch,"线程"+i));
        }
        System.out.println("启动所有任务");
        exec.shutdown();  //当所有任务执行完毕后，结束
    }
}
