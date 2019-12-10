package com.android.example.daggerlib.di;


import android.app.Application;

import com.android.example.daggerlib.AppApplication;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjectionModule;
import dagger.android.AndroidInjector;
import dagger.android.support.AndroidSupportInjection;
import dagger.android.support.AndroidSupportInjectionModule;
import dagger.android.support.DaggerApplication;

@Singleton
@Component(modules = {AndroidSupportInjectionModule.class, AppModule.class, ActivityBuildersModule.class})
public interface AppComponent extends AndroidInjector<DaggerApplication> {



    @Component.Builder
    interface Builder {
        @BindsInstance
        Builder application(Application application);
        AppComponent build();
    }

    void inject(AppApplication app);

    @Override
    void inject(DaggerApplication instance);

}
