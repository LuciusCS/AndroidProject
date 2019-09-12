package demo.lucius.androidproject.navigators;

import android.app.Activity;

import javax.inject.Inject;

import demo.lucius.androidproject.MainActivity;
import demo.lucius.androidproject.MyApplication;
import demo.lucius.androidproject.di.ActivityScope;
import demo.lucius.login.LoginNavigation;

@ActivityScope
public class MainLoginNavigation implements LoginNavigation {

    @Inject
    public MainLoginNavigation(){

    }

    @Override
    public void finishLogin(Activity activity) {
        activity.startActivity(MainActivity.getIntent(activity));
    }
}
