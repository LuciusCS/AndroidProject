package demo.lucius.androidproject.navigators;

import android.app.Activity;

import javax.inject.Inject;

import demo.lucius.function1.ui.activity.ActivityC;
import demo.lucius.funtion2.ui.navigation.Function2Navigation;

public class MainFunction2Navigation implements Function2Navigation {

    @Inject
    public MainFunction2Navigation(){

    }

    @Override
    public void goToC(Activity activity) {
        activity.startActivity(ActivityC.getIntent(activity
        ));
    }
}
