package demo.lucius.function1app.di;

import dagger.Component;
import dagger.android.AndroidInjector;
import dagger.android.ContributesAndroidInjector;
import dagger.android.support.AndroidSupportInjection;
import dagger.android.support.AndroidSupportInjectionModule;
import demo.lucius.function1app.Function1Application;

@Component(modules = {AndroidSupportInjectionModule.class,ActivitiesModule.class})
public interface ApplicationComponent extends AndroidInjector<Function1Application> {
    @Override
    void inject(Function1Application instance);

    @Component.Builder
    abstract class Builder extends AndroidInjector.Builder<Function1Application> {
//        @Override
//        public AndroidInjector<Function1Application> build() {
//            return null;
//        }
    }
}
