package demo.lucius.androidproject.di;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import demo.lucius.androidproject.navigators.MainFunction1Navigation;
import demo.lucius.function1.ui.navigation.Function1Navigation;

@Module
public interface FragmentAModule {

    @Binds
    public Function1Navigation bindFunction1Navigation(Function1Navigation function1Navigation);
}
