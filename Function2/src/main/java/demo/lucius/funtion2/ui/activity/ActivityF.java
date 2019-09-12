package demo.lucius.funtion2.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import dagger.android.support.DaggerAppCompatActivity;
import demo.lucius.funtion2.R;
import demo.lucius.funtion2.databinding.ActivityEBinding;
import demo.lucius.funtion2.databinding.ActivityFBinding;

public class ActivityF extends DaggerAppCompatActivity {

    ActivityFBinding activityFBinding;

    //用于获取启动时的上下文
    public static Intent getIntent(Context context){
        return new Intent(context,ActivityF.class);
    }


    //用于初始化界

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityFBinding= DataBindingUtil.setContentView(this, R.layout.activity_f);

    }
}
