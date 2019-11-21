package demo.lucius.baselib.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import demo.lucius.baselib.R;
import demo.lucius.baselib.databinding.ActivityCheckSignatureBinding;
import demo.lucius.baselib.module.DBBaseActivity;
import demo.lucius.baselib.viewmodel.BaseViewModel;
import demo.lucius.utilslib.log.LogUtils;

/***
 * 用于检测签名文件，防止二次打包
 */
public class CheckSignatureActivity extends DBBaseActivity<ActivityCheckSignatureBinding, BaseViewModel> {

    ActivityCheckSignatureBinding dataBinding;

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dataBinding = putContentView(R.layout.activity_check_signature);
        initView();
    }

    @Override
    protected int getBindingVariable() {
        return 0;
    }

    @Override
    protected void initView() {
       dataBinding.checkSignature.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               LogUtils.printInfo(getSignedMd5Str(CheckSignatureActivity.this));
           }
       });
    }

    @Override
    protected BaseViewModel getViewModel() {
        return null;
    }


    //获取App签名
    public static String getSignedMd5Str(Context context) {
        try {
            //得到签名
//            @SuppressLint("PackageManagerGetSignatures")
//            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(),
//                    PackageManager.GET_SIGNING_CERTIFICATES);
            @SuppressLint("PackageManagerGetSignatures")
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(),
                    PackageManager.GET_SIGNATURES);
            Signature[] signs = packageInfo.signatures;
            LogUtils.printInfo( packageInfo.toString()+"+");
            Signature signature = signs[0];
//            Toast.makeText(context,"签名信息"+packageInfo.toString(),Toast.LENGTH_LONG).show();
            return encryptionMD5(signature.toByteArray());

        } catch (Exception e) {
            LogUtils.printInfo(e.getMessage());
        }

        return "";
    }

    //使用MD5进行加密
    public static String encryptionMD5(byte[] bytes) {
        MessageDigest messageDigest=null;
        StringBuilder md5StrBuff=new StringBuilder();
        try {
            messageDigest=MessageDigest.getInstance("MD5");
            messageDigest.reset();
            messageDigest.update(bytes);
            byte[] byteArray=messageDigest.digest();
            for (byte aByteArray:byteArray){
                if (Integer.toHexString(0xff&aByteArray).length()==1){
                    md5StrBuff.append("0").append(Integer.toHexString(0xff&aByteArray));
                }else {
                    md5StrBuff.append(Integer.toHexString(0xFF & aByteArray));
                }

            }

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            LogUtils.printInfo(e.getMessage());
        }
        return md5StrBuff.toString().toUpperCase();
    }


    public native String getSignature();
}
