package com.android.example.daggerlib.db;


import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import javax.inject.Inject;


/**
 * 使用的UserDao为interface，而不是abstract ，方法也不是abstract
 */
@Dao
public abstract class UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insert(User user);

    @Query("select * from user")
    public abstract List<User> getUsers();

}
