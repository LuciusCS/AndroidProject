package demo.lucius.blemodule.ui.activity;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import demo.lucius.blemodule.R;
import demo.lucius.blemodule.databinding.ActivityBleConnectBinding;
import demo.lucius.blemodule.log.LogUtils;
import demo.lucius.blemodule.module.observe.Observer;
import demo.lucius.blemodule.module.observe.ProtocolObservable;
import demo.lucius.blemodule.module.service.BleService;
import demo.lucius.blemodule.protocol.down.BleDownMessage;
import demo.lucius.blemodule.protocol.utils.ble.BleSendCtrl;
import demo.lucius.blemodule.protocol.utils.ble.SendMessageObject;

public class BleTaskTestActivity extends AppCompatActivity implements Observer {

    private final String TAG = BleTaskTestActivity.class.getSimpleName();

    //用于请求使用蓝牙
    private final int REQUEST_ENBLE_BLE = 1;

    private BluetoothAdapter bluetoothAdapter;

    //用于表示蓝牙设备
    private List<BluetoothDevice> bluetoothDevices;

    private BleService bleService;

    //用于表示DataBinding
    ActivityBleConnectBinding activityMainBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_ble_connect);

        //用于注册观察者
        ProtocolObservable.getInstance().registerObserver(this);

        initData();
        initBle();
        initView();

    }

    //用于初始化页面
    private void initView() {
        activityMainBinding.sendInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                boolean tmp = bleService.sendBleData(BleDownMessage.bleprobe());
//                  LogUtils.printInfo(tmp+"+++++++++++++++++++");

            }
        });

        activityMainBinding.confirmTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                SendMessageObject sendMessageObject = new SendMessageObject(2, BleDownMessage.safetyCertificate());
//                  try {
//                      for (int i=0;i<10;i++) {
//                          BleMultiSendCtrl.sendQueue.put(sendMessageObject);
//                      }
//                  } catch (InterruptedException e) {
//                      e.printStackTrace();
//                  }

                //携带重发次数重发时间 ， 重发次数2  重发时间间隔5S
                SendMessageObject sendMessageObject = new SendMessageObject.Builder(2,
                        BleDownMessage.safetyCertificate()).resendTime(0).resendTimer(5).build();
                try {
                    for (int i = 0; i < 10; i++) {
                        BleSendCtrl.sendQueue.put(sendMessageObject);
                    }

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


            }
        });

        activityMainBinding.getBreakerAddressTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    //用于初始化数据
    private void initData() {
        bluetoothDevices = new ArrayList<>();

        //用于绑定Service
        Intent intent = new Intent(this, BleService.class);
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
        startService(intent);
    }


    //用于初始化蓝牙操作
    private void initBle() {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            Log.i(TAG, "BLE不可用");
            return;
        }

        if (!bluetoothAdapter.isEnabled()) {
            //请求打开蓝牙
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(intent, REQUEST_ENBLE_BLE);
            return;
        }

        //用于搜索蓝牙设备
        scanBleDevice();

        //用于在10后停止搜索
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                bluetoothAdapter.getBluetoothLeScanner().stopScan(bleScanCallback);
                LogUtils.printInfo("停止蓝牙搜索");
            }
        }, 10000);


    }

    //用于搜索蓝牙设备
    private void scanBleDevice() {
        bluetoothAdapter.getBluetoothLeScanner().stopScan(bleScanCallback);
        bluetoothAdapter.getBluetoothLeScanner().startScan(bleScanCallback);
    }

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


    //用于蓝牙设备的回调
    private ScanCallback bleScanCallback = new ScanCallback() {
        //                    LogUtils.\\("++++++++++++++++++++");
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            super.onScanResult(callbackType, result);
            LogUtils.printInfo("result：" + result.getDevice().getName());
            LogUtils.printInfo("result：" + result.getDevice().getAddress());
            LogUtils.printInfo("result：" + result.getDevice().getUuids());
            //此方法在一次扫描过程当中只会返回一台设备，也就是如果scan有结果返回后，
            //就会一直返回被第一次扫描到的那个设备，无论等多久都一样
            //如果要使用此方法的话，可能需要间歇性多次调用startScan才能发现多个设备。
            if (null != result.getDevice().getName() && result.getDevice().getName().contains("ES_CRBK")) {
                bleService.connect(bluetoothAdapter, result.getDevice().getAddress());

                bluetoothAdapter.getBluetoothLeScanner().stopScan(bleScanCallback);
//                LogUtils.printInfo(bleService.toString());
            }

//            scanBleDevice();
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

    //用于蓝牙设备的回调  LeScanCallback已经舍弃
    private BluetoothAdapter.LeScanCallback leScanCallback = new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(BluetoothDevice bluetoothDevice, int i, byte[] bytes) {
            if (!bluetoothDevices.contains(bluetoothDevice)) {
                bluetoothDevices.add(bluetoothDevice);
            }
        }
    };


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode != Activity.RESULT_OK) {
            return;
        }

        switch (requestCode) {
            case REQUEST_ENBLE_BLE:
                //用于搜索蓝牙设备
                break;
            default:
                break;

        }
    }


    //用于动态权限获取
    private void getPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (this.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    //用于获取在手机中已经获取并绑定的设备
    private void getBoundService() {
        Set<BluetoothDevice> boundDevices = bluetoothAdapter.getBondedDevices();
        for (BluetoothDevice device : boundDevices) {
            //对device进行其他操作，比如连接等。
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //用于解绑观察者
        ProtocolObservable.getInstance().unRegisterObserver(this);
    }

    //用于获取报文解析后的观察者
    @Override
    public void action(Object... objects) {
        LogUtils.printInfo(objects.toString());
        //       bleService.disconnected();
//        scanBleDevice();
    }


}
