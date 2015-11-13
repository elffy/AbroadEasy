package com.original.abroadeasy.util;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by zengjinlong on 15-10-28.
 */
public class Utils {
    public static final boolean DEBUG = true;
    public static final long SWIPE_BEHAVIOR_ANIMATION_TIME = 200;
    public static void showShortToast(Context context, int resId) {
        Toast.makeText(context, resId, Toast.LENGTH_SHORT).show();
    }
}
