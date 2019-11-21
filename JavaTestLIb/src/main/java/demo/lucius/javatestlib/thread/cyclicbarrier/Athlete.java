package demo.lucius.javatestlib.thread.cyclicbarrier;

import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class Athlete extends Thread {

    //用于表示耗时
    private double time =90;

    private static Random rand = new Random(1);

    private static CyclicBarrier cyclicBarrier;

    public Athlete(CyclicBarrier cyclicBarrier){
        this.cyclicBarrier=cyclicBarrier;
    }

    public synchronized double getTime(){
        return time;
    }

    @Override
    public void run() {
        try {
            while (!Thread.interrupted()){
                synchronized (this){
                    time =(double)(rand.nextInt(20)+90)/10;
                    System.out.println(getName()+"跑步耗时："+ getTime());
                }
                Thread.sleep(1000);
                cyclicBarrier.await();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (BrokenBarrierException e) {
            e.printStackTrace();
        }
    }
}
