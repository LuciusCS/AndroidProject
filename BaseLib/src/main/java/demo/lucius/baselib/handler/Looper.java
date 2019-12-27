package demo.lucius.baselib.handler;

public class Looper {

    public MessageQueue messageQueue;

    static final ThreadLocal<Looper>sThreadlocal=new ThreadLocal<Looper>();

    private Looper() {
        messageQueue = new MessageQueue();
    }

    public static void prepare(){
        //主线程只有唯一一个Looper对象
        if (sThreadlocal.get()!=null){
            throw new RuntimeException("只能创建一个looper");
        }

        //应用启动时，初始化赋值
        sThreadlocal.set(new Looper());

    }

    public static Looper myLooper(){
        return sThreadlocal.get();
    }

    //通过轮训提取消息
    public static void loop(){
        //从全局ThreadLocalMap中获取唯一一个Looper对象
        Looper looper=myLooper();
        //从Message中获取全局唯一一个消息队列MessageQueue
       final  MessageQueue messageQueue=looper.messageQueue;


        while (true){
            Message msg=messageQueue.next();
            if (msg!=null){
                //讲Message传送出去
                msg.target.dispatchMessage(msg);
            }
        }

    }

}
