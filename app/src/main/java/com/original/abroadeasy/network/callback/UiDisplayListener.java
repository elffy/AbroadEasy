package com.original.abroadeasy.network.callback;

import retrofit.RetrofitError;
/**
 * Created by Yang on 15/11/23.
 */

public interface UiDisplayListener<T> {
    /**
     * HTTP请求成功回调
     *
     * @param data GSON解析之后的数据model
     */
    public void onSuccessDisplay(T data);

    /**
     * HTTP请求失败回调
     */
    public void onFailDisplay(RetrofitError retrofitError);
}
