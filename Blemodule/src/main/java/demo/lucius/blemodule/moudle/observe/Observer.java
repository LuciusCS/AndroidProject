package demo.lucius.blemodule.moudle.observe;

/**
 * 用于表示观察者接口
 */
public interface Observer {

    //被观察者发生变化，通知观察者
    void action(Object... objects);

}
