package demo.lucius.javatestlib.thread.condition;

public class ThreadB extends Thread {

    MyService myService;

    public ThreadB(MyService myService) {
        this.myService = myService;
    }

    @Override
    public void run() {
        //        super.run();
        myService.awaitB();
    }
}
