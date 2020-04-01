package demo.lucius.blemodule.module;

import android.bluetooth.le.ScanResult;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * 用于表示对Ble的一些属性操作的表示
 */
public class BleDeviceInfo implements Parcelable {

    public static final String CONNECTED="BLE连接成功";
    public static final String CONNECTING="BLE连接中";
    public static final String TASK_EXEC="任务下发中";
    public static final String TASK_EXECED="任务执行完毕";

    //用于表示状态
    private String state;

    //用于表示操作次数
    private int operateTime;

    //用于表示名称
    private String bleName;

    //用于表示搜索到的蓝牙的一些属性
    private ScanResult scanResult;

    public BleDeviceInfo(String state,ScanResult scanResult){
        this.scanResult=scanResult;
        this.state=state;
    }


    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public ScanResult getScanResult() {
        return scanResult;
    }

    public void setScanResult(ScanResult scanResult) {
        this.scanResult = scanResult;
    }

    public int getOperateTime() {
        return operateTime;
    }

    public void setOperateTime(int operateTime) {
        this.operateTime = operateTime;
    }

    public String getBleName() {
        return bleName;
    }

    public void setBleName(String bleName) {
        this.bleName = bleName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

    }
}
