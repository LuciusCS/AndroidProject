package demo.lucius.baselib.handler;


/**
 * 用于HandlerMessage手写实现
 */
public class Handler {

    private Looper mLooper;

    private MessageQueue mMessageque;

    public Handler(){
            mLooper=Looper.myLooper();
            if (mLooper==null){
                throw  new RuntimeException("不能在线程"+Thread.currentThread()+"创建handler，其没有调用Looper.prepare");
            }
            mMessageque=mLooper.messageQueue;
    }




    public void sendMessage(Message message) {
        //将消息放入消息队列中
        enqueueMessage(message);
    }

    private void enqueueMessage(Message message) {
        //赋值当前Handler
        message.target=this;
        //将消息放入消息队列中
        mMessageque.enqueueMessage(message);
    }

    public void dispatchMessage(Message msg) {
        handleMessage(msg);
    }

    //给开发者提供的开放API用于重写和毁掉舰艇
    public void handleMessage(Message msg){

    }
}
