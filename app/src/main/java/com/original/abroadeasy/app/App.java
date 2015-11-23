package com.original.abroadeasy.app;

import android.app.Application;
import android.content.res.Configuration;

import com.original.abroadeasy.util.PreferenceUtils;

/**
 * Created by zengjinlong on 15-10-28.
 */
public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        PreferenceUtils.init(this);
        //DbManager.init(this);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }
}
