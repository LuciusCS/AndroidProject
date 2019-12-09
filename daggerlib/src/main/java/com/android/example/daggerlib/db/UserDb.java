package com.android.example.daggerlib.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

/***
 * 用于测试
 */

@Database(entities = {User.class}, version = 1,exportSchema = false)
public abstract  class UserDb extends RoomDatabase {

    public  abstract UserDao userDao();

}
