package demo.lucius.baselib.handler;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingDeque;

//消息队列
public class MessageQueue {

    //阻塞队列
    ArrayBlockingQueue<Message> blockingDeque=new ArrayBlockingQueue<Message>(50);

    //将Message消息对象传入阻塞队列中
    public void enqueueMessage(Message message) {
        try {
            blockingDeque.put(message);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //从消息队列中取出消息
    public Message next() {
        try {
            return blockingDeque.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }
}
