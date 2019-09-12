package demo.lucius.function1.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import dagger.android.support.DaggerAppCompatActivity;
import demo.lucius.function1.R;
import demo.lucius.function1.databinding.ActivityCBinding;

public class ActivityC extends DaggerAppCompatActivity {


    ActivityCBinding activityCBinding;

    public static Intent getIntent(Context context){
        return new Intent(context,ActivityC.class);
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityCBinding= DataBindingUtil.setContentView(this, R.layout.activity_c);
    }
}
