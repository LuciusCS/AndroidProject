package com.android.example.daggerlib.ui.detail.fragment;


import android.content.Context;
import android.os.Bundle;
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
import com.android.example.daggerlib.databinding.FragmentDetailBinding;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;

public class DetailFragment extends Fragment {

    @Inject
    ViewModelProvider.Factory viewModelFactory;


    private DetailViewModel detailViewModel;

    //用于获取DetailFragment实例
    FragmentDetailBinding fragmentDetailBinding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        return super.onCreateView(inflater, container, savedInstanceState);
        AndroidSupportInjection.inject(this);
        fragmentDetailBinding= DataBindingUtil
                .inflate(inflater, R.layout.fragment_detail, container, false);
//        View view = inflater.inflate(R.layout.fragment_detail, container, false);

        fragmentDetailBinding.insertDateTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    detailViewModel.insertUser();
            }
        });

        fragmentDetailBinding.readDataTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                detailViewModel.readUser();
            }
        });

        return fragmentDetailBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        AndroidSupportInjection.inject(this);
        detailViewModel = ViewModelProviders.of(this, viewModelFactory).get(DetailViewModel.class);
    }

}
