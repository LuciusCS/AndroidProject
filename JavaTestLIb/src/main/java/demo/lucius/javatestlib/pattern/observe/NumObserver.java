package demo.lucius.javatestlib.pattern.observe;

import java.util.Observable;
import java.util.Observer;

public class NumObserver implements Observer {
    @Override
    public void update(Observable observable, Object o) {
        NumObservable numObservable=(NumObservable)observable;
//        System.out.println("数字改变"+numObservable.getData());
        System.out.println("数字改变"+numObservable.getData());


    }
}
