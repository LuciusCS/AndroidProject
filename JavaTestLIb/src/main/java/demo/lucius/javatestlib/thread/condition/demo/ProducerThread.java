package demo.lucius.javatestlib.thread.condition.demo;

/**
 * 用于生产者
 */
public class ProducerThread extends Thread{
    private Service service;

    public ProducerThread(Service service){
        this.service=service;
    }

    @Override
    public void run() {
//        super.run();
        while (true){
            service.produce();
        }
    }
}
