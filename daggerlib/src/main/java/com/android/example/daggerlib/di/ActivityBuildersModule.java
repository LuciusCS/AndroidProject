package com.android.example.daggerlib.di;


import com.android.example.daggerlib.ui.detail.activity.MainActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivityBuildersModule {

    @ContributesAndroidInjector(modules ={ MainActivityModule.class,FragmentBuildersModule.class})
    abstract MainActivity contributeMainActivity();
}
