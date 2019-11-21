package demo.lucius.javatestlib.thread.cyclicbarrier;

import java.util.List;

public class RunningCtrl extends Thread{

    public RunningCtrl(){

    }

    @Override
    public void run() {
        super.run();
        System.out.println("++++++++++++++++++++++++++++++++");
    }
}