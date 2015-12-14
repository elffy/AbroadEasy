package com.original.abroadeasy.model;

import android.content.Context;
import android.content.SharedPreferences;

import com.original.abroadeasy.ui.UserInfoFragment;
import com.original.abroadeasy.util.PreferenceUtils;

/**
 * Created by zengjinlong on 15-11-12.
 * Use preference to store for now, if data is too much, should use database to store.
 */
public class UserInfo {

    private String mName;
    private String mPassword;// should be encrypted by MD5
    private String mDescription;
    private Object mFavorites;
    // more infos

    public UserInfo(String name, String password) {
        mName = name;
        mPassword = password;
    }

    public UserInfo(String nameAndPassword) {
        String[] strings = nameAndPassword.split(":");
        mName = strings[0];
        mPassword = strings[1];
    }

    public String getName() {
        return mName;
    }

    public String toString() {
        return mName + ":" + mPassword;
    }

    private static final String USER_INFO = "user_info";

    public static UserInfo getLoggedInUser(Context context) {
        SharedPreferences sp = PreferenceUtils.getSharedPreferences(context);
        String userInfo = sp.getString(USER_INFO, null);
        if (userInfo == null || "".equals(userInfo)) {
            return null;
        }
        return new UserInfo(userInfo);
    }

    public static void saveLoggedInUser(Context context, UserInfo info) {
        SharedPreferences sp = PreferenceUtils.getSharedPreferences(context);
        sp.edit().putString(USER_INFO, info.toString()).commit();
    }

    public static void logOut(Context context) {
        SharedPreferences sp = PreferenceUtils.getSharedPreferences(context);
        sp.edit().putString(USER_INFO, "").commit();
    }

    public static String getLoginTag(int type) {
        String tagName;
        switch (type) {
            case UserInfoFragment.LOGIN_TYPE_WEIBO:
                tagName = "SinaWeibo";
                break;
            case UserInfoFragment.LOGIN_TYPE_FACEBOOK:
                tagName = "Facebook";
                break;
            default:
                tagName = "Unavailable";
        }

        return  tagName;
    }
}
