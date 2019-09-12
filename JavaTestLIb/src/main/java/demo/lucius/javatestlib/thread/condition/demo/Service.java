package demo.lucius.javatestlib.thread.condition.demo;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;


/**
 * 有可能出现死锁
 * */
public class Service {

    private ReentrantLock lock=new ReentrantLock();
    //生产者线程
    private Condition conditionCustomer=lock.newCondition();
    //消费者线程
    private Condition conditionProducer=lock.newCondition();

    //用于表示需要生产
    private boolean hasValue=false;

    //用于生产者
    public void produce(){
        try {
            lock.lock();
            while (hasValue){
                System.out.println("生产线程："+Thread.currentThread().getName()+"await");
                conditionCustomer.await();
            }
            System.out.println("线程"+Thread.currentThread().getName()+"生产中");
            Thread.sleep(1000);
            hasValue=true;
            System.out.println("线程"+Thread.currentThread().getName()+"生产完毕");
            System.out.println("唤醒所有消费者线程 "+Thread.currentThread().getName()+"....");
            System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++.");
            conditionProducer.signalAll();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }


    }

    //用于消费者
    public void custome(){
        try{
            lock.lock();
            while (!hasValue){
                System.out.println("消费线程："+Thread.currentThread().getName()+"await");
                conditionProducer.await();
            }
            System.out.println("线程"+Thread.currentThread().getName()+"消费中");
            Thread.sleep(1000);
            hasValue=false;
            System.out.println("线程"+Thread.currentThread().getName()+"消费完毕");
            System.out.println("唤醒所有生产者线程 "+Thread.currentThread().getName()+"....");
            System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++.");
            conditionCustomer.signalAll();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            lock.unlock();
        }

    }




}
