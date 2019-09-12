package demo.lucius.androidproject.di;

import dagger.Binds;
import dagger.Module;
import demo.lucius.androidproject.navigators.MainLoginNavigation;
import demo.lucius.login.LoginNavigation;

@Module
public interface LoginActivityModule {
    @Binds
    public LoginNavigation bindLoginNavigation(MainLoginNavigation navigation);
}
