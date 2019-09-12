package demo.lucius.androidproject.di;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import demo.lucius.funtion2.ui.navigation.Function2Navigation;

@Module
public interface ActivityEModule {

    @Binds
    public Function2Navigation bindFuntion2Navigation(Function2Navigation function2Navigation);

}
