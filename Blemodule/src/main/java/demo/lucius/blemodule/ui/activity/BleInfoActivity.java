package demo.lucius.blemodule.ui.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import demo.lucius.blemodule.R;
import demo.lucius.blemodule.base.BaseViewModel;
import demo.lucius.blemodule.base.DBBaseActivity;
import demo.lucius.blemodule.databinding.ActivityBleInfoBinding;

public class BleInfoActivity extends DBBaseActivity<ActivityBleInfoBinding, BaseViewModel> {

    ActivityBleInfoBinding dataBinding;

    MutableLiveData<String> mutableLiveData=new MutableLiveData<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dataBinding=putContentView(R.layout.activity_ble_info);

//        LiveData<String>liveData=new LiveData<String>() {
//            @Nullable
//            @Override
//            public String getValue() {
//                return super.getValue();
//            }
//        };
//
//        liveData.
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
