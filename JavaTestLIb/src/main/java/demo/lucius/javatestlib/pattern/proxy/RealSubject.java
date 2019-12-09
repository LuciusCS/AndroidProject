package demo.lucius.javatestlib.pattern.proxy;

public class RealSubject implements Subject {
    @Override
    public void doSomething() {
        System.out.println("RealSubject 输出");
    }
}
