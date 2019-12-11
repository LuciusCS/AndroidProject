package com.android.example.daggerlib.ui.detail.fragment;


import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;


import com.android.example.daggerlib.R;
import com.android.example.daggerlib.bean.Bean;
import com.android.example.daggerlib.databinding.FragmentDetailBinding;
import com.android.example.daggerlib.db.User;
import com.android.example.daggerlib.db.UserDao;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasFragmentInjector;
import dagger.android.support.AndroidSupportInjection;
import dagger.android.support.DaggerFragment;

public class MainFragment extends DaggerFragment  {


//    @Inject
//    DispatchingAndroidInjector<android.app.Fragment> childFragmentInjector;

    @Inject
    ViewModelProvider.Factory viewModelFactory;

//    @Inject
//    UserDao userDao;

    private MainFragmentViewModel mainFragmentViewModel;

//    @Inject
//    Bean bean;

    //用于获取DetailFragment实例
    FragmentDetailBinding fragmentDetailBinding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        return super.onCreateView(inflater, container, savedInstanceState);
        AndroidSupportInjection.inject(this);
        fragmentDetailBinding = DataBindingUtil
                .inflate(inflater, R.layout.fragment_detail, container, false);
//        View view = inflater.inflate(R.layout.fragment_detail, container, false);

        fragmentDetailBinding.insertDateTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                    detailViewModel.insertUser();
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        User user = new User("++++");
//                        userDao.insert(user);
//                        Log.i("++", "+++");
                        mainFragmentViewModel.insertUser();
//                    }
//                }).start();

            }


        });

        fragmentDetailBinding.readDataTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        Log.i("++", userDao.getUsers().size() + "");
                        mainFragmentViewModel.readUser();

//                    }
//                }).start();
            }
        });
        return fragmentDetailBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mainFragmentViewModel = ViewModelProviders.of(this, viewModelFactory).get(MainFragmentViewModel.class);

    }

    //    @Override
//    public AndroidInjector<android.app.Fragment> fragmentInjector() {
//        return childFragmentInjector;
//    }
}
