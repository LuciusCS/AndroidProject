package com.android.example.daggerlib.ui.detail.fragment;

import android.content.Context;

import androidx.lifecycle.ViewModel;

import com.android.example.daggerlib.AppApplication;
import com.android.example.daggerlib.db.User;
import com.android.example.daggerlib.db.UserDb;
import com.android.example.daggerlib.repository.UserRepo;

import javax.inject.Inject;

public class DetailViewModel extends ViewModel {


    private UserRepo userRepo;

    //少了这一个Inject导致Fragment中的注入失败
    @Inject
    public DetailViewModel(UserRepo userRepo){
        this.userRepo=userRepo;
    }

    public void insertUser(){
        User user=new User("++++");
        userRepo.insertUser(user);
    }

    public void readUser(){
       userRepo.readUser();
    }
}
