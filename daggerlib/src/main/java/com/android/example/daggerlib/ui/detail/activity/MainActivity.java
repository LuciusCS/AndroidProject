package com.android.example.daggerlib.ui.detail.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.os.Bundle;

import com.android.example.daggerlib.R;
import com.android.example.daggerlib.bean.Bean;
import com.android.example.daggerlib.ui.detail.fragment.MainFragment;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;

public class MainActivity extends AppCompatActivity implements HasSupportFragmentInjector {

    @Inject
    DispatchingAndroidInjector<Fragment> dispatchingAndroidInjector;

    @Inject
    MainFragment mainFragment;

    @Inject
    Context context;

    @Inject
    Bean bean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidInjection.inject(this);
        setContentView(R.layout.activity_main);
        getSupportFragmentManager().beginTransaction().replace(R.id.container, mainFragment).commitAllowingStateLoss();
    }

    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        bean.printInfo();
        return dispatchingAndroidInjector;
    }
}
