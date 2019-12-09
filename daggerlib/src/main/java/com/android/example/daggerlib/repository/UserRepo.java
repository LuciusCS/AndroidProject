package com.android.example.daggerlib.repository;

import android.util.Log;

import com.android.example.daggerlib.AppExecutors;
import com.android.example.daggerlib.db.User;
import com.android.example.daggerlib.db.UserDao;
import com.android.example.daggerlib.db.UserDb;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class UserRepo {

    private final UserDb userDb;

    private final UserDao userDao;

    private final AppExecutors appExecutors;

    @Inject
    public UserRepo(UserDb userDb,UserDao userDao,AppExecutors appExecutors){
        this.userDb=userDb;
        this.userDao=userDao;
        this.appExecutors=appExecutors;
    }

    public void insertUser(final User user){
        appExecutors.getDiskIO().execute(new Runnable() {
            @Override
            public void run() {
                userDao.insert(user);
            }
        });
    }

    public void readUser(){
        appExecutors.getDiskIO().execute(new Runnable() {
            @Override
            public void run() {
                int m =userDao.getUsers().size();
                Log.i("+++","++++");
            }
        });
    }

}
