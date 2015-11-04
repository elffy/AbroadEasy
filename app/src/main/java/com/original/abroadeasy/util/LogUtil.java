package com.original.abroadeasy.util;

import android.util.Log;

/**
 * Created by zengjinlong on 15-10-30.
 */
public class LogUtil {
    private static final String TEST_TAG = "TestTag";
    public static void d(String tag, String msg) {
        if (Utils.DEBUG) {
            Log.d(tag, msg);
        }
    }

    public static void d(String msg) {
        if (Utils.DEBUG) {
            Log.d(TEST_TAG, msg);
        }
    }

    public static void e(String msg) {
        if (Utils.DEBUG) {
            Log.e(TEST_TAG, msg);
        }
    }
}
