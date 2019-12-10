package com.android.example.daggerlib.di;

import com.android.example.daggerlib.db.UserDao;
import com.android.example.daggerlib.db.UserDb;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class DbModule
{
    //用于注入UserDao
    @Singleton
    @Provides
    public UserDao provideUserDao(UserDb userDb){
        return userDb.userDao();
    }
}
