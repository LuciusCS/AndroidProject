package demo.lucius.baselib.handler;

//消息对象
public class Message {

    public int what;
   //Handler对象
    public Handler target;
  //消息内容
    public Object object;

    public Message(){

    }

    public Message(Object object){
        this.object=object;
    }
}
