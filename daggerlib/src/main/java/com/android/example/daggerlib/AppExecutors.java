package com.android.example.daggerlib;



import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.inject.Inject;
import javax.inject.Singleton;


//用于全局线程池控制
@Singleton
public class AppExecutors {

    private final Executor diskIO;

    public AppExecutors(Executor diskIO){
        this.diskIO=diskIO;
    }

    @Inject
    public AppExecutors(){
        this(Executors.newSingleThreadExecutor());
    }

    public Executor getDiskIO() {
        return diskIO;
    }
}
