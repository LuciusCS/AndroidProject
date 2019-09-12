package demo.lucius.javatestlib.thread.condition;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MyService {

    private Lock lock = new ReentrantLock();

    //使用多个Condition通知部分线程
    public Condition conditionA = lock.newCondition();
    public Condition conditionB = lock.newCondition();

    public void awaitA() {
        lock.lock();
        try {
            System.out.println("Begin awaitA的时间为" + System.currentTimeMillis() + "ThreadName" + Thread.currentThread().getName());

            conditionA.await();
            System.out.println("End awaitA的时间为" + System.currentTimeMillis() + "ThreadName" + Thread.currentThread().getName());

        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public void awaitB() {
        lock.lock();
        try {
            System.out.println("Begin awaitB的时间为" + System.currentTimeMillis() + "ThreadName" + Thread.currentThread().getName());
            System.out.println("Begin awaitB的时间为" + System.currentTimeMillis() + "ThreadName" + Thread.currentThread().getName());
            conditionB.await();
            System.out.println("End awaitB时间为" + System.currentTimeMillis() + "ThreadName" + Thread.currentThread().getName());

        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }

    }

    public void signalAll_A() {
        try {
            lock.lock();
            System.out.println("signalAll_A时间为" + System.currentTimeMillis() + "ThreadName" + Thread.currentThread().getName());
            conditionA.signalAll();
        } finally {
            lock.unlock();
        }


    }

    public void signalAll_B() {
        try {
            lock.lock();
            System.out.println("signalAll_A时间为" + System.currentTimeMillis() + "ThreadName" + Thread.currentThread().getName());
            conditionB.signalAll();
        } finally {
            lock.unlock();
        }
    }
}
