package project.lucius.aspectjlib.log;


import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import project.lucius.aspectjlib.BuildConfig;

//用于对日志日志的操作以及打印
public class LogUtils {

    private static final String LOG_UTILS = "LOG_UTILS";

    //用于表示保存文件名后缀
    private static final String FILE_NAME_SUFFIX = ".txt";


    public static void saveLogAsTxt() {
        String filePath = "/mnt/sdcard/demo";
      //  String filePath = BuildConfig.DEBUG ? "/mnt/sdcard/eastsoft" : "/data/data/com.eastsoft.pad_terminal";
//        "/data/data/com.eastsoft.pad_terminal"
        //用于表示将错误信息写入文件
        File logFile = new File( "/mnt/sdcard/demo" + File.separator + "ErrorLog" + FILE_NAME_SUFFIX);

        //用于表示将方法调用信息写入文件
        File methodFile = new File(filePath + File.separator + "MethodTrace" + FILE_NAME_SUFFIX);

        //用于表示关键方法运行
        File methodTime = new File(filePath + File.separator + "MethodTime" + FILE_NAME_SUFFIX);

        //用于表示用户的停留时间
        File residenceTimeFile = new File(filePath + File.separator + "ResidenceTime" + FILE_NAME_SUFFIX);


        try {
            Process process = Runtime.getRuntime().exec("logcat -c");
            //错误信息写入
         //   process = Runtime.getRuntime().exec("logcat -f " + logFile + " *:S ERROR:D");
            process = Runtime.getRuntime().exec("logcat -f " + logFile + " *:E");
            //方法执行过程追踪
            process = Runtime.getRuntime().exec("logcat  -f " + methodFile + " *:S METHOD:D");
            //关键方法运行时间追踪
            process = Runtime.getRuntime().exec("logcat  -f " + methodTime + " *:S RUN_TIME:D");
            //用户停留时间
            process = Runtime.getRuntime().exec("logcat  -f " + residenceTimeFile + " *:S RESIDENCE_TIME:D ");

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //用于输出Exception,会自动保存
    public static void printException(Exception e) {
        // Logger.getLogger(LogUtils.class.getName()).LogUtils(Level.SEVERE,null,e);
        StringWriter errors = new StringWriter();
        e.printStackTrace(new PrintWriter(errors));
        Log.e("Error", errors.toString());
    }

    //用于输出自定义错误，会自动保存
    public static void printException(String e) {
        //通过StackTraceElement获取方法调用者的具体信息，以下两种方法都可以使用
        //   StackTraceElement[] stackTraceElements=new Throwable().getStackTrace();
        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        Log.e("Error", stackTraceElements[3].toString() + "：" + e);
    }

    //用于输出method trace的信息
//    public static void printMethodTrace

}
