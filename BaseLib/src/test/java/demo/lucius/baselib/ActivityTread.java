package demo.lucius.baselib;

import org.junit.Test;

import demo.lucius.baselib.handler.Handler;
import demo.lucius.baselib.handler.Looper;
import demo.lucius.baselib.handler.Message;

public class ActivityTread {

    @Test
    public void main(){

        //创建全局唯一的，主线程Looper对象，以及MessageQueue消息队列
        Looper.prepare();


        //模拟Activity中，创建Handler对象
        final Handler handler=new Handler(){

            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                System.out.println(msg.object.toString());
            }
        };

        //消费消息，消费方法（接口方法）

        //子线程发送消息
        new Thread(new Runnable() {
            @Override
            public void run() {
                Message message=new Message();
                message.object="++";
                handler.sendMessage(message);
            }
        }).start();

        //轮询，取出消息
        Looper.loop();

    }
}
