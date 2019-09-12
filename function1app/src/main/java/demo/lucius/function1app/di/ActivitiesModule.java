package demo.lucius.function1app.di;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import demo.lucius.function1.ui.activity.ActivityB;
import demo.lucius.function1.ui.activity.ActivityC;
import demo.lucius.function1app.ui.activity.ActivityA;
import demo.lucius.login.LoginActivity;

@Module
interface ActivitiesModule {
    @ContributesAndroidInjector(modules = {LoginActivityModule.class})
    @ActivityScope
    public LoginActivity provideLoginActivity();

    @ContributesAndroidInjector(modules = {ActivityAModule.class})
    @ActivityScope
    public ActivityA provideActivityA();

    @ContributesAndroidInjector
    @ActivityScope
    public ActivityB provideActivityB();

    @ContributesAndroidInjector
    @ActivityScope
    public ActivityC provideActivityC();
}
