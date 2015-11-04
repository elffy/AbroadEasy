package com.original.abroadeasy;

import android.app.Application;
import android.content.res.Configuration;

/**
 * Created by zengjinlong on 15-10-28.
 */
public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
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
