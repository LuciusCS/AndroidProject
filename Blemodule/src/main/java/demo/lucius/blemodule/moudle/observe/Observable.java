package demo.lucius.blemodule.moudle.observe;

import java.lang.reflect.Array;
import java.util.ArrayList;

//用于表示被观察者接口
public abstract class Observable<T> {

    //用于表示所有的被观察者
    public final ArrayList<T> observerList=new ArrayList<>();

    //用于注册观察者对象
    public void registerObserver(T t){

        checkNull(t);
        observerList.add(t);

    }

    //用于注销观察者对象
    public void unRegisterObserver(T  t){

        checkNull(t);
        observerList.remove(t);
    }

    //用于判断观察者对象是否为空
    private void checkNull(T t){

        if (t==null){
            throw  new NullPointerException();
        }
    }

    //通知观察者做出行为改变
    public abstract void notifyObservers(Object... objects);

}
