package com.android.example.daggerlib;

import android.app.Activity;
import android.app.Application;

import com.android.example.daggerlib.di.AppComponent;
import com.android.example.daggerlib.di.DaggerAppComponent;


import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.support.DaggerApplication;


public class AppApplication extends DaggerApplication {

    @Override
    protected AndroidInjector<? extends DaggerApplication> applicationInjector() {
        AppComponent appComponent=DaggerAppComponent.builder().application(this).build();
        appComponent.inject(this);
        return appComponent;
    }
}
