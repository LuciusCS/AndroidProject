package demo.lucius.javatestlib;

/***
 * 用于测试线程之间的通信
 */
public class TestThread {

    public static Object object = new Object();


    static class Thread1 extends Thread {

        @Override
        public void run() {
//            super.run();
            synchronized (object) {
                System.out.println("线程" + Thread.currentThread().getName());
                try {
                    System.out.println("线程" + Thread.currentThread().getName() + "阻塞");
                    object.wait();
                } catch (InterruptedException e) {

                }
                System.out.println("线程" + Thread.currentThread().getName() + "执行完成");
            }

        }
    }


    static class Thread2 extends Thread {
        @Override
        public void run() {
//            super.run();
            System.out.println("线程" + Thread.currentThread().getName());
            System.out.println("线程" + Thread.currentThread().getName() + "唤醒正在阻塞的线程");
            object.notify();
            System.out.println("线程" + Thread.currentThread().getName() + "执行完成");
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Thread1 thread1 = new Thread1();
        Thread2 thread2 = new Thread2();
        thread1.start();
        Thread.sleep(2000);
        thread2.start();
    }


}
