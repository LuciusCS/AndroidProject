package com.example.glidemodule.resources;

import android.graphics.Bitmap;
import android.util.Log;

import com.example.glidemodule.utils.Tool;

/**
 * 对Bitmap的封装
 */
public class BitMapValue {

    private final String TAG= BitMapValue.class.getSimpleName();

    private BitMapValue(){}

    //使用单利模式
    private static BitMapValue bitMapValue;

    public static BitMapValue getInstance(){

        if (null== bitMapValue){
            synchronized (BitMapValue.class){
                if (bitMapValue ==null){
                    bitMapValue =new BitMapValue();
                }
            }
        }

        return bitMapValue;

    }

    private Bitmap bitmap;

    //使用计数 +1 -1
    private int count;

    //监听
    private ValueCallback valueCallback;

    //定义路径
    private String path;

    /**
     * 使用一次 +1
     */
    public void useAction(){

        Tool.checkNotEmpty(bitmap);

        if (bitmap.isRecycled()){   //已经被回收
            Log.e(TAG,"UseAction:加一 count："+count);

            return;
        }
        count++;

    }

    /**
     * 不使用或者使用完毕 -1
     * count--<=0 ，不再使用了
     */
    public void nonUseAction(){
        count--;
        if (count<=0){
            //回调给外界，，告诉不在使用了
            valueCallback.valueNonUseAction(path,this);
        };

    }

    /**
     * 释放
     */
    public void recycleBitmap(){
        if (count>0){
            Log.e(TAG,"recycleBitmap: 引用计数大于0，证明还在使用，不能释放");
            return;
        }

        if (bitmap.isRecycled()){   //被回收了
            Log.e(TAG,"Bitmap被回收了");
            return;

        }


        bitmap.recycle();

        bitMapValue =null;

        System.gc();
    }


    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public ValueCallback getValueCallback() {
        return valueCallback;
    }

    public void setValueCallback(ValueCallback valueCallback) {
        this.valueCallback = valueCallback;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
