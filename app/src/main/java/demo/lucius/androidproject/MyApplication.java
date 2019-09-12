package demo.lucius.androidproject;

import dagger.android.AndroidInjector;
import dagger.android.support.DaggerAppCompatActivity;
import dagger.android.support.DaggerApplication;
import demo.lucius.androidproject.di.DaggerApplicationComponent;

public class MyApplication extends DaggerApplication {
    @Override
    protected AndroidInjector<? extends DaggerApplication> applicationInjector() {

//        DaggerApp
//        DaggerA

//        return DaggerApplicationComponent.builder().create(this);
          return DaggerApplicationComponent.builder().create(this);
    }

}
