package demo.lucius.javatestlib.thread.cyclicbarrier;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

//用于对马匹进行控制
public class CyclicBarrierMain {

    public static void main(String[] args) {

        RunningCtrl  runningCtrl=new RunningCtrl();
        CyclicBarrier cyclicBarrier = new CyclicBarrier(5,runningCtrl);
        ExecutorService exec = Executors.newCachedThreadPool();
        for (int i = 0; i <5; i++) {
            Athlete athlete = new Athlete(cyclicBarrier);
            athlete.setName("选手："+i);
            exec.execute(athlete);
        }
    }
}
