package com.original.abroadeasy.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.original.abroadeasy.util.LogUtil;

/**
 * Author:    Yangli
 * Version    V1.0
 * Date:      15-11-23 10:53
 * Description:手机网络资源工具类
 * Modification  History:
 * Date         	Author        		Version        	Description
 * -----------------------------------------------------------------------------------
 * 15-11-23      yangli            1.0                    1.0
 * Why & What is modified:
 */
public class NetworkUtil {
    /**
     * 网络是否可用
     *
     * @param context 上下文
     * @return true 可用 false 不可用
     */
    public static boolean isNetworkAvailable(Context context) {
        if (context != null) {
            ConnectivityManager mgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo[] info = mgr.getAllNetworkInfo();
            if (info != null) {
                for (NetworkInfo anInfo : info) {
                    if (anInfo.isConnected()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
