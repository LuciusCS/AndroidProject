package demo.lucius.androidproject;

import dagger.android.AndroidInjector;
import dagger.android.support.DaggerApplication;
import demo.lucius.androidproject.di.DaggerApplicationComponent;

public class MyApplication extends DaggerApplication {
    @Override
    protected AndroidInjector<? extends DaggerApplication> applicationInjector() {
        return null;
    }
}
