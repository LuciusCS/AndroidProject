package demo.lucius.function1app;

import dagger.android.AndroidInjector;
import dagger.android.support.DaggerApplication;

public class Function1Application extends DaggerApplication {
    @Override
    protected AndroidInjector<? extends DaggerApplication> applicationInjector() {
//  return DaggerApplicationComponent.builder().create(this);
        return null;
    }
}
