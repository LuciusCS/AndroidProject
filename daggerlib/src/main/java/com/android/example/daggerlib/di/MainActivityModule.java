package com.android.example.daggerlib.di;


import com.android.example.daggerlib.bean.Bean;
import com.android.example.daggerlib.di.FragmentBuildersModule;
import com.android.example.daggerlib.ui.detail.activity.MainActivity;
import com.android.example.daggerlib.ui.detail.fragment.MainFragment;

import dagger.Module;
import dagger.Provides;
import dagger.android.ContributesAndroidInjector;

@Module
public class MainActivityModule {

    @Provides
    public Bean provide(){
        return new Bean();
    }

    @Provides
    MainFragment provideFragment(){
        return new MainFragment();
    }


}
