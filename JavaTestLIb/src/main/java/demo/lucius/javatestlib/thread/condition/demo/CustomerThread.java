package demo.lucius.javatestlib.thread.condition.demo;

public class CustomerThread extends Thread {

    private Service service;

    public CustomerThread(Service service) {
        this.service = service;
    }

    @Override
    public void run() {
        while (true){
            service.custome();
        }
    }
}
