package demo.lucius.blemodule.ui.activity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import demo.lucius.blemodule.R;
import demo.lucius.blemodule.databinding.ActivityScanBleTestBinding;
import demo.lucius.blemodule.log.LogUtils;

public class ScanBleTestActivity extends AppCompatActivity {

    //用于请求使用蓝牙
    private final int REQUEST_ENBLE_BLE = 1;

    //用于表示DataBinding
    private ActivityScanBleTestBinding scanBleBinding;

    private BluetoothAdapter bluetoothAdapter;

    //用于注册扫描的广播
    private DeviceReceiver deviceReceiver;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        scanBleBinding = DataBindingUtil.setContentView(this, R.layout.activity_scan_ble_test);
        initBle();
        startDiscover();
        //注册广播
        deviceReceiver = new DeviceReceiver();
        //广播注册的筛选信息
        IntentFilter intentFilter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(deviceReceiver, intentFilter);
    }

    //用于初始化蓝牙操作
    private void initBle() {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            return;
        }

        if (!bluetoothAdapter.isEnabled()) {
            //请求打开蓝牙
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(intent, REQUEST_ENBLE_BLE);
            return;
        }


    }

    //通用的蓝牙扫描方法
    private void startDiscover() {
        bluetoothAdapter.startDiscovery();
        //此过程大概持续10秒，当扫描到蓝牙设备后，会发出广播，只要在需要的地方注册接收广播，就可以获得扫描结果。
        //这种方法可以扫描出所有蓝牙设备，包括BLE，但貌似不同手机有不同体验
    }

    //用于注册扫描的广播
    private class DeviceReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);  //这个就是所获得的蓝牙设备。
//                mDevices.add(device );
                LogUtils.printInfo("device："+device.getName());
                LogUtils.printInfo("device："+device.getAddress());
                LogUtils.printInfo("device："+device.getUuids());
            }
        }
    }


//    //这个方法意在告诉大家获取得到设备后，我们能获得什么信息，非主要函数，可以忽略。
//    private void showDetailOfDevice(){
//        //获得设备名称，多个设备可以有同一个名称。
//        String deviceName = mTargetDevice.getName();//获得设备名称，多个设备可以有同一个名称。
//        //获取设备物理地址，一个设备只能有一个物理地址，每个设备都有每个设备的物理地址，无法相同。
//        String deviceMacAddress = mTargetDevice.getAddress();
//        //绑定设备
//        mTargetDevice.createBond();
//        //更多的信息....
//    }
}
