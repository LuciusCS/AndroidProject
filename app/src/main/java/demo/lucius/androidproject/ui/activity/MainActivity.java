package demo.lucius.androidproject.ui.activity;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import demo.lucius.androidproject.R;
import demo.lucius.androidproject.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    //用于DataBinding表示
    ActivityMainBinding activityMainBinding;


    public static Intent getIntent(Context context) {
        return new Intent(context, MainActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

    }

}
