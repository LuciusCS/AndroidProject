package demo.lucius.function1app.di;

import dagger.android.ContributesAndroidInjector;
import demo.lucius.function1.ui.fragment.FragmentA;

public interface ActivityAModule {

    @ContributesAndroidInjector(modules = {FragmentAModule.class})
    @FragmentScope
    abstract FragmentA provideFragmentA();
}
