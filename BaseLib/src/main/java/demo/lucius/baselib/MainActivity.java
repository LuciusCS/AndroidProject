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

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import demo.lucius.baselib.utils.permission.method1.PermissionUtils;
import demo.lucius.utilslib.log.LogUtils;

public class MainActivity extends AppCompatActivity {
    private BluetoothAdapter bluetoothAdapter;

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        System.out.println(stringFromJNI());
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
                //用于判断是否需要进行权限获取
                if (PermissionUtils.isRequestPermission()){
                    //权限已经获取
                    if (PermissionUtils.startRequestPermission(MainActivity.this,PermissionUtils.WRITE_EXTERNAL_STORAGE_PERMISSIONS,0)){
                        LogUtils.printInfo("权限获取后的操作");

                        //获取到文件写入操作后，写入文件夹目录
                        FileWriter fileWriter = null;
                        try {
                            File logFile = new File("/data/data/com.eastl/");

                            if(!logFile.exists()) {
                                logFile.mkdirs();
//                                logFile.mkdir();
                            }

                            fileWriter = new FileWriter("/data/data/demo.lucius.baselib/"+"测试.txt", false);
                            fileWriter.write("123");

                            fileWriter.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
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

    public native String stringFromJNI();
}
