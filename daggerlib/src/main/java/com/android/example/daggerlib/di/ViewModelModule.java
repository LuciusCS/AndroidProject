package com.android.example.daggerlib.di;


import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.android.example.daggerlib.ui.detail.fragment.MainFragmentViewModel;
import com.android.example.daggerlib.viewmodel.ViewModelFactory;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(MainFragmentViewModel.class)
    abstract ViewModel bindViewModel(MainFragmentViewModel detailViewModel);

    @Binds
    abstract ViewModelProvider.Factory bindViewModelFactory(ViewModelFactory factory);
}
