package demo.lucius.blemodule.module.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * 用于同时连接多个蓝牙同时进行处理
 */
public class MultiBleService extends Service {

    //用于同时连接5个蓝牙，各自维护一个线程
    ExecutorService exex= Executors.newFixedThreadPool(5);

    //用于控制设备诊断是否成功
    private List<Boolean>bleTaskResult;

    //用于绑定服务
//    private final IBinder mbinder = new LocalBinder();

    public class LocalBinder extends Binder{
        public MultiBleService getService(){
            return MultiBleService.this;
        }

    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    //用于启动BleTask
    public void starBleTask(){





    }


}
