package demo.lucius.baselib.utils.time;


import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import demo.lucius.utilslib.log.LogUtils;

import static android.content.Context.MODE_PRIVATE;

/***
 * 用于定时任务减少可用时间
 */
public class UseAllowCountDown extends Worker {

    private Context context;

    public UseAllowCountDown(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.context = context;
    }

    @NonNull
    @Override
    public Result doWork() {
        LogUtils.printInfo("定时任务" + System.currentTimeMillis());
        //用于定时任务每天递减
        SharedPreferences sharedPreferences = context.getSharedPreferences("allow_user_time", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        int time = sharedPreferences.getInt("allow_time", 0);

        --time;
        editor.putInt("allow_time", time);
        editor.commit();


        return Worker.Result.success();
    }
}
