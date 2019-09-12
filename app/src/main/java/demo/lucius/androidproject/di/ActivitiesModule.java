package demo.lucius.androidproject.di;


import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import demo.lucius.androidproject.MainActivity;
import demo.lucius.function1.ui.activity.ActivityB;
import demo.lucius.function1.ui.activity.ActivityC;
import demo.lucius.funtion2.ui.activity.ActivityE;
import demo.lucius.funtion2.ui.activity.ActivityF;
import demo.lucius.login.LoginActivity;

@Module
public interface ActivitiesModule {

    @ContributesAndroidInjector(modules = {LoginActivityModule.class})
    @ActivityScope
    LoginActivity provideLoginActivity();

    @ContributesAndroidInjector(modules ={MainActivityModule.class})
    @ActivityScope
    MainActivity provideMainActivity();

    @ContributesAndroidInjector
    @ActivityScope
    ActivityB provideActivityB();

    @ContributesAndroidInjector
    @ActivityScope
    ActivityC provideActivityC();

//    @ContributesAndroidInjector
//    @ActivityScope
//    ActivityD provideActivityD();

    @ContributesAndroidInjector(modules = {ActivityEModule.class})
    @ActivityScope
    ActivityE provideActivityE();

    @ContributesAndroidInjector
    @ActivityScope
    ActivityF provideActivityF();


}
