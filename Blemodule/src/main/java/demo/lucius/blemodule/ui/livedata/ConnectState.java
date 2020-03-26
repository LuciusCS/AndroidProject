package demo.lucius.blemodule.ui.livedata;

import androidx.annotation.MainThread;
import androidx.lifecycle.LiveData;

public class ConnectState extends LiveData<Boolean> {

    private static ConnectState connectState;



    @MainThread
    public static ConnectState getInstance(){
        if (connectState==null){
            connectState=new ConnectState();
        }

        return connectState;
    }

    private ConnectState(){
        connectState=new ConnectState();

    }


    @Override
    protected void onActive() {
        super.onActive();
        //当该类有一个观察者处于活动状态将会被调用
    }

    @Override
    protected void onInactive() {
        super.onInactive();
        //当该类中没有任何一个观察者处于活动状态将会调用

    }
}
