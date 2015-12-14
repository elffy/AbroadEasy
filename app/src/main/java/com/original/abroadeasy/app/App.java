package com.original.abroadeasy.app;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.bind.DateTypeAdapter;
import com.original.abroadeasy.BuildConfig;
import com.original.abroadeasy.network.RetrofitService;
import com.original.abroadeasy.util.LiteOrmDBUtil;
import com.original.abroadeasy.util.LogUtil;
import com.original.abroadeasy.util.PreferenceUtils;
import com.squareup.okhttp.OkHttpClient;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import cn.sharesdk.framework.ShareSDK;

import retrofit.ErrorHandler;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.OkClient;
import retrofit.client.Response;
import retrofit.converter.GsonConverter;

/**
 * Created by zengjinlong on 15-10-28.
 */
public class App extends Application {
    private static Context sContext;
    private static RetrofitService sRetrofitService;
    @Override
    public void onCreate() {
        super.onCreate();
        PreferenceUtils.init(this);
        LiteOrmDBUtil.init(this);
//        LiteOrmDBUtil.test();
        sContext = getApplicationContext();
        initRetrofitService();

        //Init the Mob SDK by yangli 2015.12.14
        ShareSDK.initSDK(this);
    }

    private void initRetrofitService() {
        Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, new DateTypeAdapter()).create();
        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.setConnectTimeout(15, TimeUnit.SECONDS);
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(BuildConfig.SEVER_URL)
                .setConverter(new GsonConverter(gson))
                .setClient(new OkClient(okHttpClient))
                .setErrorHandler(new MyErrorHandler())
                .build();
        sRetrofitService = restAdapter.create(RetrofitService.class);
    }

    class MyErrorHandler implements ErrorHandler {
        @Override
        public Throwable handleError(RetrofitError cause) {
            LogUtil.e("handleError:" + cause.getKind());
            Response r = cause.getResponse();
            if (r != null && r.getStatus() == 401) {
                LogUtil.e("handleError,getStatus:" + r.getStatus());
            }
            return cause;
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    public static Context getsContext() {
        return sContext;
    }

    public static App getInstance() {
        return (App)sContext;
    }

    public static RetrofitService getRetrofitService() {
        return sRetrofitService;
    }

    @Override
    public void onTerminate() {
        super.onTerminate();

        // TODO onTerminate will never be called in real phone. try another way to stop if needed.
        ShareSDK.stopSDK(this);
    }
}
