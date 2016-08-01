package com.original.abroadeasy.util;

import java.util.UUID;

import static java.util.UUID.fromString;

/**
 * Created by leo.yang on 2016/4/15.
 * 常量定义
 */
public interface ConstDefine {

    String ACTION_CONNECTED_CMD = "ACTION_SERVICE_CONNECTED_CMD";

    String ACTION_READ_CMD = "ACTION_SERVICE_READ_CMD";

    String ACTION_GPS_POST_CMD = "ACTION_GPS_POST_CMD";

    String ACTION_START_SCANNING_CMD = "ACTION_START_SCANNING_CMD";

    String ACTION_WARNING_POST_CMD = "ACTION_WARNING_POST_CMD";

    // 手机蓝牙地址(第一次获取到的)
    String BLE_ADDRESS_PREF = "mAddress_prefs";

    String LONGTITUDE_PREF = "longtitude_prefs";

    String NOTIFICATION_PREF = "notification_prefs";

    String EMPTY_STR = "";

    String UTT_SETVER_URL = "120.25.89.222/main.cgi";

    String UTT_UPGARADE_SETVER_URL = "120.25.89.222/main.cgi";

    String LATITUDE_PREF = "latitude_prefs";

    String UPGRADE_VERSION_PREF = "upgrade_version_key";

    String BLE_RSSI = "mRssi";

    String KEY_INFOR_TYPE = "infor_type_key";
    String KEY_INFOR_LONGTITUDE = "infor_longtitude_key";
    String KEY_INFOR_LATITUDE = "infor_latitude_key";
    String KEY_INFOR_RADIUS = "infor_radius_key";
    String KEY_INFOR_COUNTRYCODE = "infor_countrycode_key";
    String KEY_INFOR_COUNTRY = "infor_country_key";
    String KEY_INFOR_CITYCODE = "infor_citycode_key";
    String KEY_INFOR_CITY = "infor_city_key";
    String KEY_INFOR_DISTRICT = "infor_district_key";
    String KEY_INFOR_STREET = "infor_street_key";
    String KEY_INFOR_ADDR = "infor_addr_key";
    String KEY_INFOR_DES = "infor_description_key";

    int TYPE_GET_DEVICE_PARM = 0;
    int TYPE_GET_NUM_PARM = 1;
    int TYPE_UPLOAD_LOCATION = 2;
    int TYPE_UPLOAD_NOTIFY = 3;
    int TYPE_PUSH_MSG = 4;
    int TYPE_PARAMS_POST = 5;
    int TYPE_WARNING_NOTIFY = 6;
    int TYPE_GET_CLCIK_TIMES = 7;
    int TYPE_SET_BIND_STATE = 8;

    String TYPE_POST_TELNUM_PARM = "1";
    String TYPE_UPLOAD_LOCATION_PARM = "2";
    String TYPE_UPLOAD_NOTIFY_PARM = "3";
    String TYPE_PUSH_MSG_PARM = "4";
    String TYPE_PARAMS_POST_PARM = "5";
    String TYPE_WARNING_NOTIFY_PARM = "6";


    final int MSG_SEARCH_OUT = 0;
    final int MSG_SERCH_DONE = 1;
    final int MSG_CHA_READ = 2;
    final int MSG_CHA_SEND_LOCATION = 3;
    final int MSG_PUSH_MSG = 4;
    final int MSG_CHA_WRITE = 5;
    final int MSG_STATE_WARNING = 6;

    final int MSG_UPDATA_CLIENT = 7;
    final int MSG_GET_UNDATAINFO_ERROR = 8;
    final int MSG_DOWN_ERROR = 9;



    final int WARNING_TYPE_DEVCE_DISCONNECTED = 1;

    final int DIALOG_TYPE_GPS = 0;

    final int STATE_DEVICE_UNBIND = 0;
    final int STATE_DEVICE_BIND = 1;

    String SP_PHONE_NUMBER = "init_phone_number";

    String SP_BIND_STATE = "device_bind_state";

    String SP_POST_INTERNAL = "post_internal_value_prefs";

    String SP_AVATAR_PATH = "personal_avatar_path";

    String SP_AVATAR_NAME = "personal_avatar_name";

    String STATE_BIND = "0x00010002";

    String STATE_UNBIND = "0x00000003";

    UUID UUID_READ_SERVICE = fromString("328B1CD1-F643-F7B5-A243-0C51CD3DEEAA");

    UUID UUID_READ_SERVICE_CHARACTER = fromString("EF716109-4F4D-1882-9842-7D95E5B7DF71");

    UUID UUID_WRITE_SERVICE = fromString("000055ff-0000-1000-8000-00805f9b34fb");

    UUID UUID_WRITE_SERVICE_CHARACTER = fromString("000033f2-0000-1000-8000-00805f9b34fb");

    public static class Service {
        final static public UUID HEART_RATE = UUID.fromString("0000180d-0000-1000-8000-00805f9b34fb");
        final static public UUID BATTERY_SERVICE = UUID.fromString("0000180f-0000-1000-8000-00805f9b34fb");
        final static public UUID UNKNOWN_SERVICE = UUID.fromString("328b1cd1-f643-f7b5-a243-0c51cd3deeaa");
        final static public UUID UNKNOWN_SERVICE2 = UUID.fromString("51f6d338-f274-b387-1949-47705f0ab335");
    }

    ;

    public static class Characteristic {
        final static public UUID HEART_RATE_MEASUREMENT = UUID.fromString("00002a37-0000-1000-8000-00805f9b34fb");
        final static public UUID MANUFACTURER_STRING = UUID.fromString("00002a29-0000-1000-8000-00805f9b34fb");
        final static public UUID MODEL_NUMBER_STRING = UUID.fromString("00002a24-0000-1000-8000-00805f9b34fb");
        final static public UUID FIRMWARE_REVISION_STRING = UUID.fromString("00002a26-0000-1000-8000-00805f9b34fb");
        final static public UUID APPEARANCE = UUID.fromString("00002a01-0000-1000-8000-00805f9b34fb");
        final static public UUID BODY_SENSOR_LOCATION = UUID.fromString("00002a38-0000-1000-8000-00805f9b34fb");
        final static public UUID BATTERY_LEVEL = UUID.fromString("00002a19-0000-1000-8000-00805f9b34fb");
        final static public UUID CHAR01_LEVEL = UUID.fromString("ef716109-4f4d-1882-9842-7d95e5b7df71");
        final static public UUID CHAR02_LEVEL = UUID.fromString("5860dc9a-0ea0-4ebc-9045-f980f732932c");
        final static public UUID CHAR01_LEVEL2 = UUID.fromString("e54d1a53-e9ae-2ea1-6648-5d82466c884e");
        final static public UUID CHAR02_LEVEL2 = UUID.fromString("c8a46bff-fee2-1ca3-0c4f-be76d5afd803");
    }

    public static class Descriptor {
        final static public UUID CHAR_CLIENT_CONFIG = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb");
    }
}
