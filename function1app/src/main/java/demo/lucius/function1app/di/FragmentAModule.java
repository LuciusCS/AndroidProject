package demo.lucius.function1app.di;

import dagger.Binds;
import dagger.Module;
import demo.lucius.function1.ui.navigation.Function1Navigation;
import demo.lucius.function1app.ui.navigators.StubFunction1Navigation;

@Module
public interface FragmentAModule {
    /***
     * 变量参数写错导致
     */
    @Binds
    public Function1Navigation bindFunctionNavigation(StubFunction1Navigation stubFunction1Navigation);
}
