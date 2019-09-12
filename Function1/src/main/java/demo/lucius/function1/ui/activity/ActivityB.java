package demo.lucius.function1.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import javax.inject.Inject;

import dagger.android.DaggerActivity;
import dagger.android.support.DaggerAppCompatActivity;
import demo.lucius.function1.R;
import demo.lucius.function1.databinding.ActivityBBinding;
import demo.lucius.function1.ui.navigation.Function1Navigation;

public class ActivityB extends DaggerAppCompatActivity {

    ActivityBBinding activityBBinding;




    //用于获取启动时的上下文
    public static  Intent getIntent(Context context){
        return new Intent(context,ActivityB.class);
    }
    //用于初始化界

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityBBinding= DataBindingUtil.setContentView(this, R.layout.activity_b);
        activityBBinding.goToC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(ActivityC.getIntent(ActivityB.this));
            }
        });
    }
}
