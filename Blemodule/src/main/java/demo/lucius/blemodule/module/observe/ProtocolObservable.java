package demo.lucius.blemodule.module.observe;




/***
 * 用于报文解析后的报文信息回调，被观察者对象
 */
public class ProtocolObservable extends Observable<Observer> {

    private static ProtocolObservable protocolObservable=null;

    public static ProtocolObservable getInstance() {
        if (protocolObservable == null) {
            protocolObservable = new ProtocolObservable();
        }
        return protocolObservable;

    }

    //多线程调用单例模式的同一个方法，不是采用排队的方式，而是同时进行调用
    @Override
    public void notifyObservers(Object... objects) {
            for (Observer observer:observerList){
                if (observer!=null){
                    observer.action(objects);
                }
            }
    }
}
