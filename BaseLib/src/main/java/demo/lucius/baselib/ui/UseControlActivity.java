package demo.lucius.baselib.ui;


import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.work.Constraints;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import java.util.HashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import demo.lucius.baselib.MainActivity;
import demo.lucius.baselib.R;
import demo.lucius.baselib.databinding.ActivityUseControlBinding;
import demo.lucius.baselib.module.DBBaseActivity;
import demo.lucius.baselib.utils.encryption.AESUtil;
import demo.lucius.baselib.utils.time.NetTimeUtils;
import demo.lucius.baselib.utils.time.UseAllowCountDown;
import demo.lucius.baselib.viewmodel.BaseViewModel;
import demo.lucius.utilslib.log.LogUtils;

import static androidx.work.PeriodicWorkRequest.MIN_PERIODIC_INTERVAL_MILLIS;

/**
 * 用于控制应用的使用时间
 */
public class UseControlActivity extends DBBaseActivity<ActivityUseControlBinding, BaseViewModel> {

    ActivityUseControlBinding activityUseControlBinding;


    AlertDialog.Builder builder;

    //用于表示输入框
    EditText editText;

    AlertDialog alertDialog;

    private Runnable runnable=new Runnable() {
        @Override
        public void run() {
            LogUtils.printInfo("+++++++++++++++");
        }
    };

    private Handler handler=new Handler();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activityUseControlBinding = putContentView(R.layout.activity_use_control);



        //用于开始计时
        activityUseControlBinding.confirmTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //用于表示输入的验证码
//                String verifyCode = activityUseControlBinding.verifyCodeEt.getText().toString();
//                if (verifyCode.isEmpty()) {
//                    Toast.makeText(UseControlActivity.this, "请输入验证码", Toast.LENGTH_LONG).show();
//                    return;
//                }
//                setInstalledTime(verifyCode);
                LogUtils.printInfo("开始倒计时");
                handler.postDelayed(runnable,5000);
            }
        });
        //用于停止计时
        activityUseControlBinding.stopTimerTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogUtils.printInfo("取消倒计时");
                handler.removeCallbacks(runnable);
            }
        });

        isAllowed();


    }

    @Override
    protected int getBindingVariable() {
        return 0;
    }

    @Override
    protected void initView() {
        builder = new AlertDialog.Builder(this);
        builder.setTitle("请输入验证码");
        editText = new EditText(this);
        builder.setView(editText);
        builder.setPositiveButton("确认", null);


        alertDialog = builder.create();
        alertDialog.setCancelable(false);
        alertDialog.show();

        //截取确认按钮使其不消失
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogUtils.printInfo("点击确认");
                //用于表示输入的验证码
                String verifyCode = editText.getText().toString();
                if (verifyCode.isEmpty()) {
                    Toast.makeText(UseControlActivity.this, "请输入验证码", Toast.LENGTH_LONG).show();
                    return;
                }
                LogUtils.printInfo(verifyCode);
                setInstalledTime(verifyCode);
            }
        });
    }

    @Override
    protected BaseViewModel getViewModel() {
        return null;
    }

    //用于判断是否可用
    private boolean isAllowed() {
        //获取允许的时间戳
        SharedPreferences sharedPreferences = getSharedPreferences("allow_user_time", MODE_PRIVATE);
        int time = sharedPreferences.getInt("allow_time", 0);
//        if (time > 0) {
//            startActivity(new Intent(this, MainActivity.class));
//            finish();
//        } else {
//            Toast.makeText(this, "请连接网络并进行认证", Toast.LENGTH_LONG).show();
//            initView();
//        }

        LogUtils.printInfo("剩余时间：" + time + "天");

        if (time == 0) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            //设置可用时间
            editor.putInt("allow_time", 100);
            editor.commit();
            setCountDownTask();
        }
        //用于开启定时任务


        return true;
    }

    //进行应用安装使用时间配置
    private void setInstalledTime(String verify) {


        //用于获取网络时间
        long netTime = 0;
        try {
            netTime = NetTimeUtils.getNetTime();
            LogUtils.printInfo(String.valueOf(netTime));
            //用于判断返回的数据是否为9999999，如果不是则启动24小时定时任务
            if (netTime == 99999999) {
                Toast.makeText(this, "请连接到有效网络", Toast.LENGTH_LONG).show();
                return;
            }

        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //用于获取认证的截止时间
        String decode = AESUtil.decrypt(verify);
        Long verifyTime = 0l;
        if (decode == null) {
            Toast.makeText(this, "请输入有效验证码", Toast.LENGTH_LONG).show();
            return;
        } else {
            verifyTime = Long.valueOf(decode);
        }
        //用于表示可以使用的天数
        int dayLength = (int) ((verifyTime - netTime) / 1000l / 60l / 60l / 24l);

        if (dayLength > 0 && dayLength <= 30) {

            LogUtils.printInfo("可用时间" + dayLength);

            SharedPreferences sharedPreferences = getSharedPreferences("allow_user_time", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            //设置可用时间
            editor.putInt("allow_time", dayLength);
            editor.commit();

            //用于开启定时任务
            setCountDownTask();
            alertDialog.dismiss();
            startActivity(new Intent(this, MainActivity.class));

            finish();
        } else {
            Toast.makeText(this, "请输入有效验证码", Toast.LENGTH_LONG).show();
        }
    }

    //用于开启任务进行倒计时
    private void setCountDownTask() {
        SharedPreferences sharedPreferences = this.getSharedPreferences("workmanager", Context.MODE_PRIVATE);
        boolean isFirstStart = sharedPreferences.getBoolean("workmanager", true);
        if (isFirstStart) {
//            WorkManager workManager = WorkManager.getInstance();
            WorkManager workManager = WorkManager.getInstance(this);
            Constraints constraints = new Constraints.Builder().build();

            //定时任务最小时间间隔为15分钟，如果再小就会失效（源码）
            PeriodicWorkRequest dataCheckBuilder = new PeriodicWorkRequest.Builder(UseAllowCountDown.class,
                    1, TimeUnit.HOURS).setConstraints(constraints).build();

            workManager.enqueue(dataCheckBuilder);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("workmanager", false);
            editor.commit();
        }
    }


}
