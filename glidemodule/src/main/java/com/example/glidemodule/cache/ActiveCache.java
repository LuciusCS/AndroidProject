package com.example.glidemodule.cache;

import android.graphics.Bitmap;

import com.example.glidemodule.resources.BitMapValue;
import com.example.glidemodule.resources.ValueCallback;
import com.example.glidemodule.utils.Tool;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

/**
 * 活动缓存 -- 真正正在使用的资源
 */
public class ActiveCache {


    //容器
    private Map<String, WeakReference<BitMapValue>>mapList=new HashMap<>();

    private Map<String, Bitmap>mapValueList=new HashMap<>();

    private ReferenceQueue<BitMapValue>queue;    //目的：为了监听弱引用是否被回收

    private boolean isCloseThread;

    private Thread thread;

    //用于判断是主动还是被动移除
    private boolean isAutoRemove;

    private ValueCallback valueCallback;

    public ActiveCache(ValueCallback valueCallback){
        this.valueCallback=valueCallback;
    }

    /**
     * TODO 添加活动缓存
     * @param path
     * @param bitMapValue
     */
    public void put(String path, BitMapValue bitMapValue){

        Tool.checkNotEmpty(path);

        //绑定Value的监听 --》 Value发起来的(Value没有被使用了，就会发起这个坚挺，给外界业务需要来使用)
        bitMapValue.setValueCallback(valueCallback);

        //存储 --》 容器
        mapList.put(path,new CustomWeakReference(bitMapValue,getQueue(),path));

        mapValueList.put(path,bitMapValue.getBitmap());
    }


    /**
     * TODO 给外界获取value
     * @param path
     * @return
     */
    public BitMapValue get(String path){
        WeakReference<BitMapValue>valueWeakReference=mapList.get(path);
        if (null!=valueWeakReference){
            BitMapValue value=valueWeakReference.get();   //返回value
            value.setBitmap(mapValueList.get(path));
            value.setPath(path);
            return value;   //返回value
        }
        return null;

    }

    /**
     * TODO 手动删除
     * @param key
     * @return
     */
    public BitMapValue remove(String key){

        isAutoRemove=false;
        WeakReference<BitMapValue>valueWeakReference= mapList.remove(key);
        isAutoRemove=true;  //还原，目的让GC 自动移除继续工作，
        if (null!=valueWeakReference){
            return valueWeakReference.get();

        }
        return null;
    }

    /**
     * 主动释放内存缓存，并将线程进行关闭
     */

    public void closeActiveCache(){
        isCloseThread=true;
        mapList.clear();;
        System.gc();
    }


    //监听弱引用，成为弱引用的子类，为了监听弱引用是否被回收,并获取弱引用回收后的 path和 BitMapValue
    //使用弱引用子类，是为了把构造方法
    public class CustomWeakReference extends WeakReference<BitMapValue>{

        private String path;
        private BitMapValue value;

        public CustomWeakReference(BitMapValue referent, ReferenceQueue<? super BitMapValue> q,String path) {
            super(referent, q);
            this.path =path;
            this.value=referent;
        }

    }


    /**
     * 创建弱引用队列，并对弱引用的回收进行监听
     * @return
     */
    private ReferenceQueue<BitMapValue>getQueue(){

        if (queue==null){
            queue=new ReferenceQueue<>();

            thread= new Thread(){
                @Override
                public void run() {
                    super.run();
                    while (!isCloseThread){
                        try {

                            if (!isAutoRemove) {
                                //阻塞式方法 如果有引用被回收，则会执行该方法，并返回被回收的对象
                                Reference<? extends BitMapValue> remove = queue.remove();

                                CustomWeakReference weakReference = (CustomWeakReference) remove;
                                //移除容器  //isAutoRemove 区分手动移除还是被动移除
                                if (mapList != null && !mapList.isEmpty()) {
                                    mapList.remove(weakReference.path);
                                    mapValueList.remove(weakReference.path);
                                }
                            }

                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            };
            thread.start();
        }
        return queue;
    }
}
