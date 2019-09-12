package demo.lucius.baselib;

import android.bluetooth.BluetoothAdapter;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import demo.lucius.baselib.utils.permission.method1.PermissionUtils;
import demo.lucius.utilslib.log.LogUtils;

public class MainActivity extends AppCompatActivity {
    private BluetoothAdapter bluetoothAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //用于进入时获取权限
        if (PermissionUtils.isRequestPermission()){
            //权限已经获取
            if (PermissionUtils.startRequestPermission(this,PermissionUtils.LOCATION_PERMISSIONS,0)){
                LogUtils.printInfo("权限获取后的操作");
            }else {
             //权限未获取
                LogUtils.printInfo("权限未获取");

            }
        }


        //用于点击事件获取权限
        findViewById(R.id.get_permission_tv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (PermissionUtils.isRequestPermission()){
                    //权限已经获取
                    if (PermissionUtils.startRequestPermission(MainActivity.this,PermissionUtils.LOCATION_PERMISSIONS,0)){
                        LogUtils.printInfo("权限获取后的操作");

                    }else {
                        //权限未获取
                        LogUtils.printInfo("权限未获取");

                    }
                }

            }
        });
    }

    //用户申请权限回调方法

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        LogUtils.printInfo("权限请求回调");
        if (PermissionUtils.requestPermissionResult(this,permissions,grantResults)){

            LogUtils.printInfo("权限请求回调");
            if (requestCode==0){

            }
        }
    }
}
