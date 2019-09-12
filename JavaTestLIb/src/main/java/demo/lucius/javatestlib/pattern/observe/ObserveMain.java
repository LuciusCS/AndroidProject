package demo.lucius.javatestlib.pattern.observe;

/**
 * 用于测试设计模式中的观察者模式
 */
public class ObserveMain {
    public static void main(String[] args) {
        NumObservable numObservable=new NumObservable();
        numObservable.addObserver(new NumObserver());

        numObservable.setData(1);
        numObservable.setData(2);
        numObservable.setData(3);


    }
}
