package com.original.abroadeasy.app;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.bind.DateTypeAdapter;
import com.original.abroadeasy.BuildConfig;
import com.original.abroadeasy.network.http.AppApiService;
import com.original.abroadeasy.util.PreferenceUtils;
import com.original.abroadeasy.util.SharedPreferencesHelper;
import com.squareup.okhttp.OkHttpClient;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import retrofit.RestAdapter;
import retrofit.client.OkClient;
import retrofit.converter.GsonConverter;

import static android.content.SharedPreferences.*;

/**
 * Created by zengjinlong on 15-10-28.
 */
public class App extends Application {
    private static Context sContext;
    private static AppApiService sAppApiService;
    @Override
    public void onCreate() {
        super.onCreate();
        PreferenceUtils.init(this);
        //DbManager.init(this);
        sContext = getApplicationContext();

        setupSharePreferenceHelper(getApplicationContext());
        setUpApiService();


    }

    private void setUpApiService() {
        Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, new DateTypeAdapter()).create();
        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.setConnectTimeout(15, TimeUnit.SECONDS);
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(BuildConfig.API_HOST)
                .setConverter(new GsonConverter(gson))
                .setClient(new OkClient(okHttpClient))
                .build();
        sAppApiService = restAdapter.create(AppApiService.class);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    private void setupSharePreferenceHelper(Context context) {
        SharedPreferencesHelper.getInstance().Builder(context);
    }

    public static Context getsContext() {
        return sContext;
    }

    public static App getInstance() {
        return (App)sContext;
    }

    public static AppApiService getsAppApiService() {
        return sAppApiService;
    }
}
