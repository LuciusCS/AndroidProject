package demo.lucius.javatestlib.pattern.proxy;

public class Client {

    public static void main(String[] args) {
//        RealSubject proxy=new ProxyTest();
//        proxy.doSomething();
        ProxyHandler proxyHandler=new ProxyHandler();
        //绑定该类实现的所有接口
        Subject sub=(Subject)proxyHandler.bind(new RealSubject());
        sub.doSomething();
    }
}
