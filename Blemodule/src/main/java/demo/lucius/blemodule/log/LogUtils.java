package demo.lucius.blemodule.log;

import android.util.Log;

public class LogUtils {


    //用于输出自定义错误
    public static void printInfo(String e) {
        //通过StackTraceElement获取方法调用者的具体信息，以下两种方法都可以使用
        //   StackTraceElement[] stackTraceElements=new Throwable().getStackTrace();
        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        Log.e("Error", stackTraceElements[3].toString() + "：" + e);
    }
}
