package demo.lucius.function1app.ui.navigators;

import android.app.Activity;

import javax.inject.Inject;

import demo.lucius.function1app.di.ActivityScope;
import demo.lucius.function1app.ui.activity.ActivityA;
import demo.lucius.login.LoginNavigation;

@ActivityScope
public class Function1LoginNavigation implements LoginNavigation {

    @Inject
    public Function1LoginNavigation(){}

    @Override
    public void finishLogin(Activity activity) {
        activity.startActivity(ActivityA.getIntent(activity));
    }
}
