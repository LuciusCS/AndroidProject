package demo.lucius.javatestlib.thread.condition;


public class TestMain {

    public static void main(String[] args) throws InterruptedException {

        MyService myService = new MyService();

        ThreadA threadA = new ThreadA(myService);
        ThreadB threadB = new ThreadB(myService);

        threadA.setName("Thread A");
        threadB.setName("Thread B");

        threadA.start();
        threadB.start();

        Thread.sleep(3000);
        myService.signalAll_A();

//        myService.signalAll_A();

    }

}
