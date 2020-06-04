package demo.lucius.blemodule.ui.activity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.le.ScanResult;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.view.View;

import androidx.annotation.Nullable;

import demo.lucius.blemodule.R;
import demo.lucius.blemodule.base.BaseViewModel;
import demo.lucius.blemodule.base.DBBaseActivity;
import demo.lucius.blemodule.databinding.ActivityBleTaskBinding;
import demo.lucius.blemodule.log.LogUtils;
import demo.lucius.blemodule.module.service.BleService;
import demo.lucius.blemodule.protocol.down.BleDownMessage;
import demo.lucius.blemodule.protocol.utils.ble.BleSendCtrl;
import demo.lucius.blemodule.protocol.utils.ble.SendMessageObject;
import demo.lucius.blemodule.ui.view.dialog.LoadingDialog;

/***
 * 启动之后就进行连接认证操作
 */
public class BleTaskActivity extends DBBaseActivity<ActivityBleTaskBinding, BaseViewModel> {

    ActivityBleTaskBinding activityBleTaskBinding;

    private BluetoothAdapter bluetoothAdapter;

    private ScanResult scanResult;

    private BleService bleService;

    //用于表示初始化进度
    private LoadingDialog loadingDialog;


    //用于连接到服务
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            bleService = ((BleService.LocalBinder) iBinder).getService();

        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            bleService = null;

        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityBleTaskBinding = putContentView(R.layout.activity_ble_task);
        initData();
        initView();
    }

    @Override
    protected int getBindingVariable() {
        return R.layout.activity_ble_task;

    }

    //用于初始化数据
    private void initData() {
        scanResult = (ScanResult) getIntent().getExtras().get("SCAN_RESULT");
        //用于绑定Service
        Intent intent = new Intent(this, BleService.class);
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);

        //Service的启动需要一点时间
        startService(intent).getClassName();
        //用于连接到蓝牙设备
//        bleService.connect(bluetoothAdapter,scanResult.getDevice().getAddress());

        LogUtils.printInfo(scanResult.toString());
    }

    @Override
    protected void initView() {
        setActionBar(true, "单设备诊断");
        activityBleTaskBinding.setResult(scanResult);

        activityBleTaskBinding.bleNameTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                bleService.sendBleData( BleDownMessage.bleprobe());
            }
        });


        activityBleTaskBinding.assignTaskTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                assignTask();
            }
        });

        //用于进行连接操作
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                LogUtils.printInfo("启动连接操作");
                initBle();
            }
        }, 500);

    }

    /***
     * 自动进行的连接认证，任务
     * @return
     */
    private void initBle() {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        bleService.connect(bluetoothAdapter, scanResult.getDevice().getAddress());
    }


    /***
     * 用于下发任务，
     * @return
     */
     private void assignTask(){

         //携带重发次数重发时间 ， 重发次数2  重发时间间隔3S，认证帧
         SendMessageObject sendMessageObject = new SendMessageObject.Builder(2,
                 BleDownMessage.safetyCertificate()).resendTime(1).resendTimer(3).build();
         //试探帧
         SendMessageObject sendMessageObject1 = new SendMessageObject.Builder(SendMessageObject.BLE_DIRECT_SEND ,
                 BleDownMessage.bleprobe()).build();

         try {
//             BleSendCtrl.sendQueue.put(sendMessageObject1);
             BleSendCtrl.sendQueue.put(sendMessageObject);
//             for (int i = 0; i < 10; i++) {
//                 BleMultiSendCtrl.sendQueue.put(sendMessageObject);
//             }

         } catch (InterruptedException e) {
             e.printStackTrace();
         }
     }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        BleSendCtrl.sendQueue.clear();
        BleSendCtrl.resendQueue.clear();
        bleService.disconnected();
    }

    /**
     * 返回后停止任务
     * @return
     */


    @Override
    protected BaseViewModel getViewModel() {
        return null;
    }
}
