package demo.lucius.function1app.di;

import dagger.Binds;
import dagger.Module;
import demo.lucius.function1app.ui.navigators.Function1LoginNavigation;
import demo.lucius.login.LoginNavigation;

@Module
public interface LoginActivityModule {

    @Binds
    public LoginNavigation provideLoginNavigation(Function1LoginNavigation loginNavigation);
}
