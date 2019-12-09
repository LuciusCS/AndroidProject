package demo.lucius.javatestlib.pattern.proxy;

public class ProxyTest extends RealSubject {
    @Override
    public void doSomething() {
        System.out.println("proxy输出");
        super.doSomething();
    }
}
