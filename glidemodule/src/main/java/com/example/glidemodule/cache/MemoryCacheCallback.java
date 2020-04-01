package com.example.glidemodule.cache;

import androidx.annotation.NonNull;

import com.example.glidemodule.resources.BitMapValue;

/**
 * 内存缓存中，元素被移除的接口回调
 */
public interface MemoryCacheCallback {

    /**
     * 内存缓存中移除的path
     * @param path
     * @param oldBitMapValue
     */
    public void entryRemoveMemoryCache(String path,@NonNull BitMapValue oldBitMapValue);
}
