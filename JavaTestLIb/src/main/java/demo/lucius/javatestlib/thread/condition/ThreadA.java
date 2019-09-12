package demo.lucius.javatestlib.thread.condition;

public class ThreadA extends Thread {

    private MyService myService;

    public ThreadA(MyService myService) {
        super();
        this.myService = myService;
    }

    @Override
    public void run() {
//        super.run();
        myService.awaitA();
    }
}
