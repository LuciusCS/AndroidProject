package project.lucius.aspectjlib.exception;


import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import project.lucius.aspectjlib.BuildConfig;
import project.lucius.aspectjlib.log.LogUtils;

//用于对崩溃信息的记录
public class CrashUtils  implements Thread.UncaughtExceptionHandler  {

    //用于表示CrashUtils单例模式
    public static CrashUtils INSTANCE=new CrashUtils();

    //用于表示存储文件的后缀
    private static final String FILE_NAME_SUFFIX=".crash";

    //系统默认UncaughtException处理类
    Thread.UncaughtExceptionHandler uncaughtExceptionHandler;

    //用于表示上下文环境
    private Context context;

    //用于表示设备信息和异常信息
    private Map<String, String> errorInfos = new HashMap<>();


    private DateFormat format = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");

    //用于表示单例
    public static CrashUtils getInstance(){
        return INSTANCE;
    }


    @Override
    public void uncaughtException(Thread t, Throwable e) {
        LogUtils.printException("系统崩溃");

        //如果自定义的处理函数没有处理完，使用系统默认处理方式
        if (!handleException(e) && uncaughtExceptionHandler != null) {
            //自己没有处理完交给系统
            LogUtils.printException("系统崩溃");
        } else {
            //主动处理
            try {
                Thread.sleep(2000);

            } catch (Exception e1) {

            }
            //可以杀死进程并退出
//            Intent intent = new Intent(context.getApplicationContext(), TestShowItemActivity.class);
//
//            @SuppressLint("WrongConstant") PendingIntent pendingIntent = PendingIntent.getActivity(context
//                            .getApplicationContext(), 0, intent,
//                    Intent.FLAG_ACTIVITY_NEW_TASK);
//            //退出程序
//            AlarmManager alarmManager=(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
//            alarmManager.set(AlarmManager.RTC,System.currentTimeMillis()+1000,pendingIntent);  //1S后程序重启
//
//            //退出程序
//            android.os.Process.killProcess(android.os.Process.myPid());
//            System.exit(1);

        }
    }

    //用于调用时的初始化
    public void init(Context context){

        this.context = context;

        //获取系统默认的UncatchException
        uncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();

        //设置该工具为默认处理器
        Thread.setDefaultUncaughtExceptionHandler(this);

    }

    //用于处理异常信息
    private boolean handleException(Throwable throwable) {
        if (throwable == null) {
            return false;
        }

        //使用Toast显示异常信息
        new Thread() {
            @Override
            public void run() {
                Looper.prepare();
                Toast.makeText(context, "程序出现异常,退出.",
                        Toast.LENGTH_SHORT).show();
                Looper.loop();
            }
        }.start();

        //用于将错误信息保存至txt
        saveExceptionToTxt(throwable);
        return true;
    }


    //用于收集设备信息参数
    public void collectDeviceInfo(Context context) {

        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), PackageManager
                    .GET_ACTIVITIES);
            if (packageInfo != null) {
                String versionName = packageInfo.versionName == null ? "null" : packageInfo.versionName;
                String versionCode = String.valueOf(packageInfo.versionCode);
                errorInfos.put("versionName", versionName);
                errorInfos.put("versionCode", versionCode);
            }

        } catch (Exception e) {

        }

        //获取所有系统信息
        Field[] fields = Build.class.getDeclaredFields();
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                errorInfos.put(field.getName(), field.get(null).toString());
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

    }

    //用于将错误信息保存至文件中
    private void saveExceptionToTxt(Throwable throwable) {


        String time = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss").format(new Date());

        String filePath = BuildConfig.DEBUG ? "/mnt/sdcard/eastsoft/" : "/data/data/com.eastsoft.pad_terminal/";

        //用于表示需要写入的文件
        File file = new File(filePath + File.separator + time + FILE_NAME_SUFFIX);
        //将错误内容写到文件中
        try {
            PrintWriter printWriter = new PrintWriter(new BufferedWriter(new FileWriter(file)));
            //保存时间
            printWriter.print(time);

            //保存错误内容
            throwable.printStackTrace(printWriter);

            printWriter.close();

        } catch (Exception e) {
            Log.i("写入文件异常", e.getMessage());
        }
    }


    //用于获取当前进程的名称
    private String getCurrentProcessName(Context context){
        try {
            int pid =android.os.Process.myPid();
            ActivityManager activityManager=(ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
            for(ActivityManager.RunningAppProcessInfo appProcessInfo:activityManager.getRunningAppProcesses()){
                if (appProcessInfo.pid==pid){
                    return appProcessInfo.processName;
                }
            }
        }catch (Exception e){

        }
        return "";
    }

}
