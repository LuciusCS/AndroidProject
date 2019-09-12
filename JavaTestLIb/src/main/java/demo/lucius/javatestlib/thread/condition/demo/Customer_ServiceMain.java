package demo.lucius.javatestlib.thread.condition.demo;

public class Customer_ServiceMain {

    public static void main(String[] args) throws InterruptedException {

        Service service=new Service();

        CustomerThread[] customerThread=new CustomerThread[10];
        ProducerThread[] producerThreads=new ProducerThread[10];
        for (int i=0;i<3;i++){
            customerThread[i]=new CustomerThread(service);
//            customerThread[i].setName("Producer Thread"+i);
            producerThreads[i]=new ProducerThread(service);
//            producerThreads[i].setName("Customer Thread"+i);
            customerThread[i].start();
            producerThreads[i].start();
//            producerThreads[i].;

        }

    }
}
