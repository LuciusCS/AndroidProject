package demo.lucius.androidproject.navigators;

import android.app.Activity;

import javax.inject.Inject;

import demo.lucius.androidproject.di.ActivityScope;
import demo.lucius.function1.ui.navigation.Function1Navigation;
import demo.lucius.funtion2.ui.activity.ActivityF;

@ActivityScope
public class MainFunction1Navigation implements Function1Navigation {

    @Inject
    public MainFunction1Navigation(){

    }

    @Override
    public void goToF(Activity activity) {
        activity.startActivity(ActivityF.getIntent(activity));
    }
}
