package com.android.example.daggerlib.di;


import com.android.example.daggerlib.ui.detail.fragment.MainFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class FragmentBuildersModule {

    @ContributesAndroidInjector
    abstract MainFragment contributeDetailFragment();
}
