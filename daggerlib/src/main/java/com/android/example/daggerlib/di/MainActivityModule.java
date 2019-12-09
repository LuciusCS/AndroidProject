package com.android.example.daggerlib.di;


import com.android.example.daggerlib.di.FragmentBuildersModule;
import com.android.example.daggerlib.ui.detail.activity.MainActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class MainActivityModule {

//    @Provides
//    Repo provideRepo(UserDao userDao ) {
//        return new Repo(userDao);
//    }

    @ContributesAndroidInjector(modules = FragmentBuildersModule.class)
    abstract MainActivity contributeMainActivity();

}
