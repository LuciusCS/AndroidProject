package demo.lucius.baselib.utils.permission.method1;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import demo.lucius.utilslib.log.LogUtils;

/**
 * 应用动态权限获取，方法一
 */
public class PermissionUtils {

    /**
     * 相机需获取的权限
     */
    public static final String[] CAMERA_PERMISSIONS = {Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};
    /**
     * 获取系统状态的权限
     */
    public static final String[] PHONE_STATE_PERMISSIONS = {Manifest.permission.READ_PHONE_STATE};
    /**
     * 录音权限
     */
    public static final String[] RECORD_AUDIO_PERMISSIONS = {Manifest.permission.RECORD_AUDIO};
    /**
     * 读写存储权限
     */
    public static final String[] WRITE_EXTERNAL_STORAGE_PERMISSIONS = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
    /**
     * 定位权限
     */
    public static final String[] LOCATION_PERMISSIONS = {Manifest.permission.ACCESS_COARSE_LOCATION};

    //用于BLE权限获取
    public static final String[] BLE_PERMISSION={Manifest.permission.BLUETOOTH};

    //用于检查是否需要动态获取权限
    public static boolean isRequestPermission() {
        //只有系统API大于23时，才需要判断权限是否需要获取
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }


    //开始提交请求权限
    public static boolean startRequestPermission(Activity activity, String[] permissions, int requestCode) {
        //检查该权限是否已经获取
        int i = ContextCompat.checkSelfPermission(activity, permissions[0]);
        //权限是否已经获取 GRANTED--授权 DINED--拒绝
        if (i != PackageManager.PERMISSION_GRANTED) {
            //如果没有被授予该权限，提示用户请求该权限
           LogUtils.printInfo("用户进行权限申请");
            ActivityCompat.requestPermissions(activity, permissions, requestCode);

            return false;
        } else {
            return true;
        }
    }

    //用于权限请求结果回调
    public static boolean requestPermissionResult(Activity activity, @NonNull String[] permissions,
                                                  @Nullable int[] grantResult) {

        //未获取到权限
        if (grantResult[0] != PackageManager.PERMISSION_GRANTED) {
            //判断用户是否点击了不再提醒；检测该权限是否还可以再申请
            boolean result = activity.shouldShowRequestPermissionRationale(permissions[0]);

            //如果不可以再申请
            if (!result) {
                //用户需要继续使用App
                //提示用户去应用设置界面手动开启权限，各大厂商权限开启的方法各不相同，需要进行不同厂商适配，因此只给予提示
                LogUtils.printInfo("请到设置界面开启相应权限");
                return false;
            }else {
                //如果可以再申请
                LogUtils.printInfo("下次开取权限");
                return false;
            }
        }else {
            return true;
        }
    }


}
