package demo.lucius.blemodule.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import demo.lucius.blemodule.R;
import demo.lucius.blemodule.base.BaseViewModel;
import demo.lucius.blemodule.base.DBBaseActivity;
import demo.lucius.blemodule.databinding.ActivityMainBinding;

public class MainActivity extends DBBaseActivity<ActivityMainBinding, BaseViewModel> {

   ActivityMainBinding activityMainBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding=putContentView(R.layout.activity_main);
        initView();
//        ContextCompat.get
    }

    @Override
    protected int getBindingVariable() {
        return R.layout.activity_main;
    }


    @Override
    protected void initView() {
       setActionBar(false,"蓝牙操作");
       activityMainBinding.bleOperatorTv.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
              startActivity(new Intent(MainActivity.this,BleScanActivity.class));
           }
       });

       activityMainBinding.bleDiagnoseTv.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               startActivity(new Intent(MainActivity.this,BleMultiTaskActivity.class));
           }
       });

       activityMainBinding.bleAutoTaskTv.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               startActivity(new Intent(MainActivity.this,BleAutoTaskActivity.class));
           }
       });

    }

    @Override
    protected BaseViewModel getViewModel() {
        return null;
    }
}
