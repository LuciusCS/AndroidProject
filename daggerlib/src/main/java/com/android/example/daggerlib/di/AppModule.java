package com.android.example.daggerlib.di;


import android.app.Application;
import android.content.Context;

import androidx.room.Room;

import com.android.example.daggerlib.AppApplication;
import com.android.example.daggerlib.db.UserDao;
import com.android.example.daggerlib.db.UserDb;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(includes = ViewModelModule.class)
public class AppModule {

//    @Provides
//    @Singleton
//    public Context provideContext(Application application) {
//        return application;
//    }

    //用于注入DB单例

    //这里只能用Application 而非AppApplication，用user_db而非user.db
    @Singleton
    @Provides
    public UserDb provideDb(Application application) {
        return Room.databaseBuilder(application, UserDb.class, "user.db").build();
    }

    //用于注入UserDao
    @Singleton
    @Provides
    public UserDao provideUserDao(UserDb userDb){
        return userDb.userDao();
    }

}
