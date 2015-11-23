package com.original.abroadeasy.network.callback;

import com.original.abroadeasy.util.LogUtil;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Yang on 15/11/23.
 */
public class HttpBaseCallBack<T> implements Callback<T> {
    protected String TAG = HttpBaseCallBack.class.getSimpleName();

    @Override
    public void success(T t, Response response) {
        LogUtil.d(TAG, "success--> url = " + response.getUrl());
    }

    @Override
    public void failure(RetrofitError retrofitError) {
        LogUtil.d(TAG, "failure--> url = " + retrofitError.getUrl());
        retrofitError.printStackTrace();
    }
}
