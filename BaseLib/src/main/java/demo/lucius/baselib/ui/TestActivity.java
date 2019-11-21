package demo.lucius.baselib.ui;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;

import demo.lucius.baselib.R;
import demo.lucius.baselib.databinding.ActivityTestViewBinding;
import demo.lucius.baselib.module.DBBaseActivity;
import demo.lucius.baselib.viewmodel.BaseViewModel;

public class TestActivity extends DBBaseActivity<ActivityTestViewBinding, BaseViewModel> {

    private ActivityTestViewBinding testViewBinding;

    private LiveData<String>test=Constant.test;

    /**
     * 在这一个方法下对databinding初始化会报错
     * @param savedInstanceState
     */
//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
//        super.onCreate(savedInstanceState, persistentState);
//    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        testViewBinding=putContentView(R.layout.activity_test_view);
        testViewBinding.setInfo(test);
        testViewBinding.setLifecycleOwner(this);
        testViewBinding.test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Constant.test.postValue(Constant.test.getValue()+"++++");
                System.out.println(Constant.test.getValue());
                System.out.println(test.getValue());
            }
        });

    }

    @Override
    protected int getBindingVariable() {
        return 0;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected BaseViewModel getViewModel() {
        return null;
    }
}
