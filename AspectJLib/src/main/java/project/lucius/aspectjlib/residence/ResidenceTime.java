package project.lucius.aspectjlib.residence;


//用于对页面停留时间进行打印

import android.util.Log;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

@Aspect
public class ResidenceTime {

    private static final String TAG = "RESIDENCE_TIME";

    @Before("execution(* android.app.Activity.onCreate(..))")
    public void activityOnCreateTime(JoinPoint joinPoint) {
        Log.i(TAG, "onCreate()" + joinPoint.toString());
    }

    @Before("execution(* android.support.v7.app.AppCompatActivity.onDestroy(..))")
    public void activityOnDestoryTime(JoinPoint joinPoint) {
        Log.i(TAG, "onDestroy()" + joinPoint.toString());
    }

//    @Before("execution(* android.support.v4.app.Fragment.onCreate(..)")
//    public void fragmentOnCreateTime(JoinPoint joinPoint) {
//        Log.i(TAG, "onCreate()" + joinPoint.toString());
//    }
//
//    //    onStop()
//    @Before("execution(* android.support.v4.app.Fragment.onStop(..)")
//    public void fragmentOnStopTime(JoinPoint joinPoint) {
//        Log.i(TAG, "onStop()" + joinPoint.toString());
//    }
}
