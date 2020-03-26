package demo.lucius.blemodule.ui.activity;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

public class TestLiveDataActivity extends AppCompatActivity {

    MutableLiveData<String> mutableLiveData=new MutableLiveData<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mutableLiveData.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Log.i("输出数据：",s);
            }
        });
//      final TestLiveDataBean testLiveDataBean=new TestLiveDataBean();
//
//        testLiveDataBean.setValue();



        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(5000);
                    TestLiveDataBean testLiveDataBean=new TestLiveDataBean();
//                    testLiveDataBean.removeObserve();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }).start();
    }
}
