package com.original.abroadeasy.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by zengjinlong on 15-11-5.
 */
public class PreferenceUtils {

    private static final String SHARED_PREFS_NAME = "proud_enjoy_prefs";

    private static final String FIRST_LAUNCH = "first_launch";

    public static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);
    }

    public static boolean isFirstLaunch(Context context) {
        SharedPreferences sp = getSharedPreferences(context);
        boolean ret = sp.getBoolean(FIRST_LAUNCH, true);
        if (ret) {
            sp.edit().putBoolean(FIRST_LAUNCH, false).commit();
        }
        return ret;
    }
}
