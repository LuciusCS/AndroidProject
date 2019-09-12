package demo.lucius.blemodule.moudle.observe;




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
    @Override
    public void notifyObservers(Object... objects) {

            for (Observer observer:observerList){
                if (observer!=null){
                    observer.action(objects);
                }
            }
    }
}
