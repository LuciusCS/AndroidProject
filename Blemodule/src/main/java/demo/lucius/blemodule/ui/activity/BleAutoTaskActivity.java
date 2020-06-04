package demo.lucius.blemodule.ui.activity;


import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import demo.lucius.blemodule.R;
import demo.lucius.blemodule.base.BaseViewModel;
import demo.lucius.blemodule.base.DBBaseActivity;
import demo.lucius.blemodule.databinding.ActivityBleAutoTaskBinding;
import demo.lucius.blemodule.log.LogUtils;
import demo.lucius.blemodule.module.BleDeviceInfo;
import demo.lucius.blemodule.module.observe.Observer;
import demo.lucius.blemodule.module.observe.ProtocolObservable;
import demo.lucius.blemodule.module.service.BleService;
import demo.lucius.blemodule.protocol.down.BleDownMessage;
import demo.lucius.blemodule.protocol.utils.ble.BleSendCtrl;
import demo.lucius.blemodule.protocol.utils.ble.SendMessageObject;
import demo.lucius.blemodule.ui.view.adapter.BleListAdapter;
import demo.lucius.blemodule.ui.view.dialog.LoadingDialog;
import demo.lucius.blemodule.utils.databus.LiveDataBus;


/**
 * 用于BLE自动连接认证
 */
public class BleAutoTaskActivity extends DBBaseActivity<ActivityBleAutoTaskBinding, BaseViewModel> implements Observer {


    private BluetoothAdapter bluetoothAdapter;

    //用于表示蓝牙搜索到的设备
//    List<ScanResult> scanResults;

    //用于表示蓝牙搜索到的设备，带有操作信息
    List<BleDeviceInfo> bleDeviceInfos;

    //用于表示蓝牙列表判断是否加入到ScanResult中
    Set<BluetoothDevice> bluetoothDevices;

    //用于表示蓝牙列表适配器
    BleListAdapter bleListAdapter;

    ActivityBleAutoTaskBinding dataBinding;
    AlertDialog.Builder builder;
    AlertDialog alertDialog;


    //用于表示进度框
    LoadingDialog loadingDialog;

    //用于表示蓝牙列表的RecyclerView
    private RecyclerView bleRecyclerView;

    //用于表示后台蓝牙操作服务
    private BleService bleService;

    //用于表示当前操作BLE的设备
//    private ScanResult mScanResult;
    private BleDeviceInfo mbleDeviceInfo;

    //用于表示是不是正在进行扫描操作
    private boolean scaning=false;

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
        dataBinding = putContentView(R.layout.activity_ble_auto_task);
        initBle();
        initData();
        initView();
    }

    @Override
    protected int getBindingVariable() {
        return 0;
    }

    private void initData() {
        //用于注册观察者
        ProtocolObservable.getInstance().registerObserver(this);
//        scanResults = new ArrayList<>();
        bluetoothDevices = new HashSet<>();
        bleDeviceInfos = new ArrayList<>();
        //用于绑定Service
        Intent intent = new Intent(this, BleService.class);
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);

        //Service的启动需要一点时间
        startService(intent).getClassName();
        initLiveData();
    }

    @Override
    protected void initView() {

//        bleListAdapter = new BleListAdapter(scanResults);
        bleListAdapter = new BleListAdapter(bleDeviceInfos);
        //用于表示蓝牙列表信息
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_auto_task, null);
        bleRecyclerView = view.findViewById(R.id.ble_device_rv);
        bleRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        bleRecyclerView.setAdapter(bleListAdapter);


        //用于蓝牙设备对话框
        builder = new AlertDialog.Builder(this);
        builder.setView(view);
        builder.setNegativeButton("关闭", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        alertDialog = builder.create();
        alertDialog.setCancelable(true);
        alertDialog.show();


        loadingDialog = new LoadingDialog(this, "BLE搜索中", R.drawable.loading);
        loadingDialog.show();

        scanBleDevice();
    }

    @Override
    protected BaseViewModel getViewModel() {
        return null;
    }

    //用于蓝牙设备的回调
    private ScanCallback bleScanCallback = new ScanCallback() {

        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            super.onScanResult(callbackType, result);
            LogUtils.printInfo("result：" + result.getDevice().getName());

            //此方法在一次扫描过程当中只会返回一台设备，也就是如果scan有结果返回后，
            //就会一直返回被第一次扫描到的那个设备，无论等多久都一样
            //如果要使用此方法的话，可能需要间歇性多次调用startScan才能发现多个设备。
            if (null != result && null != result.getDevice().getName() && result.getDevice().getName().contains(
                    "ES_CRBK") && bluetoothDevices.add(result.getDevice())) {

                //如果是第一个扫描到的设备
                BleDeviceInfo bleDeviceInfoTmp = new BleDeviceInfo("等待中", result);
                bleDeviceInfoTmp.setBleName("断路器");
                if (bleDeviceInfos.size() == 0) {
                    bleDeviceInfoTmp.setOperateTime(1);
                    bleDeviceInfoTmp.setState("BLE连接中");
                    mbleDeviceInfo = bleDeviceInfoTmp;

                    bleService.connect(bluetoothAdapter, result.getDevice().getAddress());
                }

                bleDeviceInfos.add(bleDeviceInfoTmp);
                LogUtils.printInfo(bleListAdapter.getItemCount() + "+++++++");
                bleListAdapter.notifyDataSetChanged();
            }

        }

        @Override
        public void onBatchScanResults(List<ScanResult> results) {
            super.onBatchScanResults(results);
            LogUtils.printInfo("result：" + results.size());
            //在此返回一个包含所有扫描结果的列表集，包括以往扫描到的结果。
            for (int i = results.size() - 1; i >= 0; i--) {
                LogUtils.printInfo(results.get(i).getDevice().getName());
                LogUtils.printInfo(results.get(i).getDevice().getAddress());

            }
        }

        @Override
        public void onScanFailed(int errorCode) {
            super.onScanFailed(errorCode);
            LogUtils.printInfo("errorCode：" + errorCode);
            //扫描失败后的处理。
        }
    };

    //用于初始化蓝牙操作
    private void initBle() {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
//            Log.i(TAG, "BLE不可用");
            return;
        }

        if (!bluetoothAdapter.isEnabled()) {
            //请求打开蓝牙
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
//            startActivityForResult(intent, REQUEST_ENBLE_BLE);
            return;
        }

    }

    //用于搜索蓝牙设备
    private void scanBleDevice() {
        bluetoothAdapter.getBluetoothLeScanner().stopScan(bleScanCallback);
        bluetoothAdapter.getBluetoothLeScanner().startScan(bleScanCallback);
        scaning=true;


        //用于在15s之后停止搜索
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                bluetoothAdapter.getBluetoothLeScanner().stopScan(bleScanCallback);
                loadingDialog.setmMessage("BLE自动处理中");
                LogUtils.printInfo("停止蓝牙搜索");
                scaning=false;

                //判断有没有没连接认证成功的
                for (int i = 0; i < bleDeviceInfos.size(); i++) {
                    if (!bleDeviceInfos.get(i).getState().equals("任务执行完毕")) {
                        if (bleDeviceInfos.get(i).getOperateTime() < 3) {
                            mbleDeviceInfo = bleDeviceInfos.get(i);
                            bleService.connect(bluetoothAdapter,
                                    mbleDeviceInfo.getScanResult().getDevice().getAddress());
                            break;
                        }
                    }
                    //如果没有需要进行连接的
                    if (i==bleDeviceInfos.size()-1&&!scaning){
                        loadingDialog.dismiss();
//                        alertDialog.dismiss();
                        LogUtils.printInfo("任务执行完毕");
                    }
                }

                if (bleDeviceInfos.size() == 0) {
                    loadingDialog.dismiss();
//                    alertDialog.dismiss();
                    Toast.makeText(BleAutoTaskActivity.this, "环境内无蓝牙设备", Toast.LENGTH_LONG).show();
                }
            }
        }, 15000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //用于解绑观察者
        ProtocolObservable.getInstance().unRegisterObserver(this);
    }


    //用于初始化livedata
    private void initLiveData() {

        LiveDataBus.get().with("ble_key").postValue(String.valueOf("1"));
        LiveDataBus.get().with("ble_key", String.class)
                .observe(this, new androidx.lifecycle.Observer<String>() {
                    @Override
                    public void onChanged(@Nullable String s) {
                        LogUtils.printInfo(s);
                        switch (s) {
                            case "ble_connected":
                                mbleDeviceInfo.setState("BLE连接成功");
                                bleListAdapter.notifyDataSetChanged();
                                break;
                            case "ble_prob":
                                Toast.makeText(BleAutoTaskActivity.this, "试探帧发送成功", Toast.LENGTH_LONG).show();
                                LogUtils.printInfo("试探帧发送成功");
                                mbleDeviceInfo.setState("任务下发中");
                                bleListAdapter.notifyDataSetChanged();
                                assignTask();
                                break;
                            case "ble_verify":
                                Toast.makeText(BleAutoTaskActivity.this, "蓝牙认证成功", Toast.LENGTH_LONG).show();
                                LogUtils.printInfo("蓝牙认证成功");
                                mbleDeviceInfo.setState("任务执行完毕");
                                bleListAdapter.notifyDataSetChanged();

                                bleService.disconnected();
                                break;
                            case "ble_disconnected":
                                int tmp = bleDeviceInfos.indexOf(mbleDeviceInfo);
                                if (tmp < bleDeviceInfos.size() - 1) {

                                    mbleDeviceInfo = bleDeviceInfos.get(tmp + 1);
                                    mbleDeviceInfo.setState("连接中");
                                    mbleDeviceInfo.setOperateTime(mbleDeviceInfo.getOperateTime() + 1);
                                    bleService.connect(bluetoothAdapter,
                                            mbleDeviceInfo.getScanResult().getDevice().getAddress());
                                    bleListAdapter.notifyDataSetChanged();
                                    LogUtils.printInfo("进行下一个蓝牙连接");
                                } else {
                                    //判断有没有没连接认证成功的
                                    for (int i = 0; i < bleDeviceInfos.size(); i++) {
                                        if (!bleDeviceInfos.get(i).getState().equals("任务执行完毕")) {
                                            LogUtils.printInfo("进入到任务排查阶段");
                                            if (bleDeviceInfos.get(i).getOperateTime() < 3) {
                                                mbleDeviceInfo = bleDeviceInfos.get(i);
                                                mbleDeviceInfo.setOperateTime(mbleDeviceInfo.getOperateTime() + 1);
                                                bleService.connect(bluetoothAdapter,
                                                        mbleDeviceInfo.getScanResult().getDevice().getAddress());
                                                break;
                                            }else {
                                                mbleDeviceInfo.setState("蓝牙操作失败");
                                                bleListAdapter.notifyDataSetChanged();
                                            }
                                        }
                                        //如果没有需要进行连接的
                                        if (i==bleDeviceInfos.size()-1&&!scaning){
                                            loadingDialog.dismiss();
//                                            alertDialog.dismiss();
                                            LogUtils.printInfo("任务执行完毕");
                                        }
                                    }
                                }
                                break;
                            default:
                                break;

                        }


                    }
                });
    }

    @Override
    public void action(Object... objects) {
    }

    /***
     * 用于下发任务，
     * @return
     */
    private void assignTask() {
        //携带重发次数重发时间 ， 重发次数2  重发时间间隔3S，认证帧
        SendMessageObject sendMessageObject = new SendMessageObject.Builder(2,
                BleDownMessage.safetyCertificate()).resendTime(1).resendTimer(3).build();
        //试探帧
        SendMessageObject sendMessageObject1 = new SendMessageObject.Builder(SendMessageObject.BLE_DIRECT_SEND,
                BleDownMessage.bleprobe()).build();

        try {
            BleSendCtrl.sendQueue.put(sendMessageObject1);
            BleSendCtrl.sendQueue.put(sendMessageObject);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
