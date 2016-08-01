package com.original.abroadeasy.app;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.bind.DateTypeAdapter;
import com.kf5sdk.api.CallBack;
import com.kf5sdk.init.KF5SDKConfig;
import com.kf5sdk.init.KF5SDKInitializer;
import com.kf5sdk.init.UserInfo;
import com.kf5sdk.utils.SDKPreference;
import com.original.abroadeasy.BuildConfig;
import com.original.abroadeasy.model.UserAccount;
import com.original.abroadeasy.network.RetrofitService;
import com.original.abroadeasy.util.LiteOrmDBUtil;
import com.original.abroadeasy.util.LogUtil;
import com.original.abroadeasy.util.PreferenceUtils;
import com.original.abroadeasy.util.SharedPreferencesHelper;
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
    public static int sScreenHeight;
    @Override
    public void onCreate() {
        super.onCreate();
        PreferenceUtils.init(this);
        LiteOrmDBUtil.init(this);
//        LiteOrmDBUtil.test();
        sContext = getApplicationContext();
        initRetrofitService();
        sScreenHeight = getResources().getDisplayMetrics().heightPixels;

        setUpSharedPreferencesHelper(this);

        //Init the Mob SDK by yangli 2015.12.14
        ShareSDK.initSDK(this);

        // Init the KF5 SDK
        initForKF5();
    }

    /**
     * 初始化SharedPreferences
     *
     * @param context 上下文
     */
    private void setUpSharedPreferencesHelper(Context context) {
        SharedPreferencesHelper.getInstance().Builder(context);

    }

    private void initForKF5() {
        KF5SDKInitializer.initialize(this);
        // init User for KF5 SDK. TODO, complete it later
        UserInfo userInfo = UserAccount.getKF5TestUser();
        KF5SDKConfig.INSTANCE.init(this, userInfo, new CallBack() {
            @Override
            public void onSuccess(String s) {
                LogUtil.d("onSuccess:" + s);
            }

            @Override
            public void onFailure(String s) {
                LogUtil.d("onFailure:" + s);
            }
        });
        // TODO, just for test, remove it later.
        SDKPreference.setLoginSuccess(true, this);
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
