package demo.lucius.javatestlib.pattern.observe;

import java.util.Observable;

public class NumObservable extends Observable {

    private int data;

    public int getData() {
        return data;
    }

    public void setData(int data) {
        this.data = data;
        setChanged();
        notifyObservers();
    }
}
