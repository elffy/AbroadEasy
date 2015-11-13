package com.original.abroadeasy.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by zengjinlong on 15-11-5.
 */
public class PreferenceUtils {

    private static final String SHARED_PREFS_NAME = "proud_enjoy_prefs";

    private static final String FIRST_LAUNCH = "first_launch";

    private static SharedPreferences sSp;

    public static void init(Context context) {
        sSp = context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);
    }
    public static SharedPreferences getSharedPreferences(Context context) {
        if (sSp == null) {
            init(context);
        }
        return sSp;
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
