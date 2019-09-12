package demo.lucius.androidproject.di;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import demo.lucius.function1.ui.fragment.FragmentA;

@Module
public interface MainActivityModule {

    @ContributesAndroidInjector(modules = {FragmentAModule.class})
    @FragmentScope
    abstract FragmentA provideFragmentA();
}
