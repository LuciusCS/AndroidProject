package demo.lucius.function1app.ui.navigators;

import android.app.Activity;

import javax.inject.Inject;

import demo.lucius.function1.ui.navigation.Function1Navigation;
import demo.lucius.function1app.di.ActivityScope;

@ActivityScope
public class StubFunction1Navigation implements Function1Navigation {
   @Inject
    public  StubFunction1Navigation(){

   }

    @Override
    public void goToF(Activity activity) {

    }
}

