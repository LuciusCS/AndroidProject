package com.example.glidemodule.resources;

/**
 * 专门给 Value 不再使用的回调借口
 */
public interface ValueCallback {

    /**
     * 监听的方法（Value不再使用了）
     * @param path
     * @param bitMapValue
     */
    public void valueNonUseAction(String path, BitMapValue bitMapValue);
}
