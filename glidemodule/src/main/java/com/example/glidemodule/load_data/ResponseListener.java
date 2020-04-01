package com.example.glidemodule.load_data;

import com.example.glidemodule.resources.BitMapValue;

/**
 * 加载外部资源成功与失败的 回调
 */
public interface ResponseListener {

    public void responseSuccess(BitMapValue bitMapValue);

    public void responseException(Exception e);

}
