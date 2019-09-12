package demo.lucius.androidproject.di;


import android.app.Application;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjection;
import dagger.android.AndroidInjector;
import dagger.android.support.AndroidSupportInjection;
import dagger.android.support.AndroidSupportInjectionModule;
import demo.lucius.androidproject.MyApplication;

@Component(modules = {AndroidSupportInjectionModule.class, ActivitiesModule.class})
public interface ApplicationComponent extends AndroidInjector<MyApplication> {

    @Override
    void inject(MyApplication instance);

    @Component.Builder
    abstract class Builder extends AndroidInjector.Builder<MyApplication> {
    }
}
