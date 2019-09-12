package demo.lucius.funtion2.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import javax.inject.Inject;

import dagger.android.support.DaggerAppCompatActivity;
import demo.lucius.funtion2.R;
import demo.lucius.funtion2.databinding.ActivityEBinding;
import demo.lucius.funtion2.ui.navigation.Function2Navigation;

public class ActivityE extends DaggerAppCompatActivity {

    ActivityEBinding activityEBinding;

//    @Inject
//    Function2Navigation function2Navigation;

    //用于获取启动时的上下文
    public static Intent getIntent(Context context){
        return new Intent(context,ActivityE.class);
    }


    //用于初始化界
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityEBinding= DataBindingUtil.setContentView(this, R.layout.activity_e);
       activityEBinding.goToC.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
//               function2Navigation.goToC(ActivityE.this);
           }
       });

       activityEBinding.goToF.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
                startActivity(ActivityF.getIntent(ActivityE.this));
           }
       });

    }
}
