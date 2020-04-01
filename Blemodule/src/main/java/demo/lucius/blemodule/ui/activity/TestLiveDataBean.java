package demo.lucius.blemodule.ui.activity;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import demo.lucius.blemodule.log.LogUtils;

/**
 * 用于测试LiveData，无生命周期
 */
public class TestLiveDataBean {

    MutableLiveData<String> mutableLiveData = new MutableLiveData<>();

    Observer<String> observer;

    public TestLiveDataBean() {
        observer = new Observer<String>() {
            @Override
            public void onChanged(String s) {
                LogUtils.printInfo(s);
            }
        };
        mutableLiveData.observeForever(observer);
    }

    public void setValue() {
        mutableLiveData.setValue("测试");
    }

    public void removeObserve()
    {
        LogUtils.printInfo("remove observer");
        mutableLiveData.removeObserver(observer);
    }
}
