package demo.lucius.blemodule.ui.activity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import demo.lucius.blemodule.R;
import demo.lucius.blemodule.base.BaseViewModel;
import demo.lucius.blemodule.base.DBBaseActivity;
import demo.lucius.blemodule.databinding.ActivityBleScanBinding;
import demo.lucius.blemodule.log.LogUtils;
import demo.lucius.blemodule.ui.view.adapter.BleListAdapter;

public class BleScanActivity extends DBBaseActivity<ActivityBleScanBinding, BaseViewModel> implements BleListAdapter.OnClickListener {

    private final String TAG = BleScanActivity.class.getSimpleName();

    ActivityBleScanBinding databinding;

    //用于请求使用蓝牙
    private final int REQUEST_ENBLE_BLE = 1;


    private BluetoothAdapter bluetoothAdapter;

    //用于表示蓝牙搜索到的设备
    List<ScanResult> scanResults;

    //用于表示蓝牙列表判断是否加入到ScanResult中
    Set<BluetoothDevice> bluetoothDevices;

    //用于表示蓝牙列表适配器
    BleListAdapter bleListAdapter;


    //用于蓝牙设备的回调
    private ScanCallback bleScanCallback = new ScanCallback() {
        //                    LogUtils.\\("++++++++++++++++++++");
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            super.onScanResult(callbackType, result);
            LogUtils.printInfo("result：" + result.getDevice().getName());
            //此方法在一次扫描过程当中只会返回一台设备，也就是如果scan有结果返回后，
            //就会一直返回被第一次扫描到的那个设备，无论等多久都一样
            //如果要使用此方法的话，可能需要间歇性多次调用startScan才能发现多个设备。
            if (null != result && null != result.getDevice().getName() && result.getDevice().getName().contains(
                    "ES_CRBK") && bluetoothDevices.add(result.getDevice())) {
//                bleService.connect(bluetoothAdapter, result.getDevice().getAddress());
                scanResults.add(result);
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


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        databinding = putContentView(R.layout.activity_ble_scan);
        initData();
        initBle();
        initView();

    }

    @Override
    protected int getBindingVariable() {
        return 0;
    }


    private void initData() {

        scanResults = new ArrayList<>();

        bluetoothDevices = new HashSet<>();
    }

    @Override
    protected void initView() {
        setActionBar(true, "蓝牙搜索");
        databinding.bleDeviceRv.setLayoutManager(new LinearLayoutManager(this));
//        bleListAdapter = new BleListAdapter(scanResults);
        bleListAdapter.setOnClickListener(this);
        databinding.bleDeviceRv.setAdapter(bleListAdapter);
        databinding.scanBleTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LogUtils.printInfo("蓝牙扫描");
                scanResults.clear();
                bluetoothDevices.clear();
                bleListAdapter.notifyDataSetChanged();
                scanBleDevice();

            }
        });

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

    }

    //用于搜索蓝牙设备
    private void scanBleDevice() {
        bluetoothAdapter.getBluetoothLeScanner().stopScan(bleScanCallback);
        bluetoothAdapter.getBluetoothLeScanner().startScan(bleScanCallback);

        //用于在15s之后停止搜索
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                bluetoothAdapter.getBluetoothLeScanner().stopScan(bleScanCallback);
                LogUtils.printInfo("停止蓝牙搜索");
            }
        }, 15000);
    }

    @Override
    protected BaseViewModel getViewModel() {
        return null;
    }

    @Override
    public void onClickListener(ScanResult scanResult) {
        bluetoothAdapter.getBluetoothLeScanner().stopScan(bleScanCallback);
        Intent intent = new Intent(this, BleTaskActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable("SCAN_RESULT", scanResult);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}
