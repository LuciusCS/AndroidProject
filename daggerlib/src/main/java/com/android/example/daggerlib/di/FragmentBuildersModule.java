package com.android.example.daggerlib.di;


import com.android.example.daggerlib.ui.detail.fragment.DetailFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class FragmentBuildersModule {

    @ContributesAndroidInjector
    abstract DetailFragment contributeDetailFragment();
}
