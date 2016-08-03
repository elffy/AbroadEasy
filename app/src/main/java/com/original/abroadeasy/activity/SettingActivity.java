package com.original.abroadeasy.activity;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.preference.RingtonePreference;
import android.preference.SwitchPreference;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;


import com.original.abroadeasy.R;
import com.original.abroadeasy.upgrade.UpdateInfo;
import com.original.abroadeasy.upgrade.UpdateUtil;
import com.original.abroadeasy.util.ConstDefine;
import com.original.abroadeasy.util.LogUtil;
import com.original.abroadeasy.util.SharedPreferencesHelper;
import com.original.abroadeasy.view.AlertDialogCreator;

import java.io.File;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * A {@link PreferenceActivity} that presents a set of application settings. On
 * handset devices, settings are presented as a single list. On tablets,
 * settings are split by category, with category headers shown to the left of
 * the list of settings.
 * <p/>
 * See <a href="http://developer.android.com/design/patterns/settings.html">
 * Android Design: Settings</a> for design guidelines and the <a
 * href="http://developer.android.com/guide/topics/ui/settings.html">Settings
 * API Guide</a> for more information on developing a Settings UI.
 * 设置页面，用于设置一些常规的参数，比如连接手环的地址显示，是否打开通知等，目前把它作为二级页面
 * By yangli
 */
public class SettingActivity extends AppCompatPreferenceActivity implements Preference.OnPreferenceChangeListener, ConstDefine {

    private SwitchPreference mNewMessagePref;
    private SwitchPreference mEnableAutoLinkPref;
    private SwitchPreference mVibratePref;
    private RingtonePreference mRingtonePref;
    private ListPreference mSyncPref;
    private Preference mAddressPrefs;
    private Preference mUpgradeVersionPrefs;
    // Menu entries
    private static final int MENU_RESTORE_DEFAULTS    = 1;

    private SharedPreferencesHelper sharedPreferencesHelper;


    public static final String KEY_NEW_MESSAGE     = "notifications_new_message";
    public static final String KEY_AUTO_LINK= "auto_link";
    public static final String KEY_VIBRATE    = "notifications_new_message_vibrate";
    public static final String KEY_RINGTONE= "notifications_new_message_ringtone";
    public static final String KEY_SYNC_FREQUENCE    = "sync_frequency";
    public static final String KEY_DEVICE_ADDRESS    = "mac_address_key";

    private Activity pThis;

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        boolean result = false;
        if (preference == mNewMessagePref) {
            LogUtil.d("mNewMessagePref" + newValue);
            if (null != sharedPreferencesHelper) {
                boolean notificationPrefs = (boolean) newValue;
                sharedPreferencesHelper.putBoolean(NOTIFICATION_PREF, notificationPrefs);
                result = true;
            }
        } else if (preference == mSyncPref) {
            final String summary = newValue.toString();
            int internal = 0;
            int index = mSyncPref.findIndexOfValue(summary);
            mSyncPref.setSummary(mSyncPref.getEntries()[index]);
            mSyncPref.setValue(summary);
            switch (index) {
                case 0:
                    internal = 15000;
                    break;
                case 1:
                    internal = 20000;
                    break;
                case 2:
                    internal = 60000;
                    break;
                case 3:
                    internal = 180000;
                    break;
                case 4:
                    internal = 360000;
                    break;

            }
            if (null != sharedPreferencesHelper) {
                /*if (null != App.timerTask) {
                    LogUtil.d("times == " + internal);
                    App.timerTask.setPeriod(internal);
                }*/
                sharedPreferencesHelper.putInt(SP_POST_INTERNAL, internal);
                result = true;
            }
        }
        return result;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pThis = this;
        loadPrefs();
        setTitle("设置");
        setupActionBar();
    }

    private void loadPrefs() {
        LogUtil.d("loadPrefs");
        addPreferencesFromResource(R.xml.pref_headers);
        sharedPreferencesHelper = SharedPreferencesHelper.getInstance();

        mEnableAutoLinkPref = (SwitchPreference) findPreference(KEY_AUTO_LINK);

        mNewMessagePref = (SwitchPreference) findPreference(KEY_NEW_MESSAGE);
        mVibratePref = (SwitchPreference) findPreference(KEY_VIBRATE);
        //获取硬件地址
        String bindAddress = sharedPreferencesHelper.getString(BLE_ADDRESS_PREF);
        mAddressPrefs = (Preference) findPreference(KEY_DEVICE_ADDRESS);
        mAddressPrefs.setSummary(bindAddress);

        mUpgradeVersionPrefs = (Preference) findPreference(UPGRADE_VERSION_PREF);
        mUpgradeVersionPrefs.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                CheckVersionTask checkVersionTask = new CheckVersionTask();
                Thread UpgradeThread = new Thread(checkVersionTask);
                UpgradeThread.start();
                return false;
            }
        });



        mRingtonePref = (RingtonePreference) findPreference(KEY_RINGTONE);
        mRingtonePref.setShowDefault(false);
        mSyncPref
                = (ListPreference) findPreference(KEY_SYNC_FREQUENCE);
        mSyncPref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                final String summary = newValue.toString();
                int internal = 0;
                int index = mSyncPref.findIndexOfValue(summary);
                mSyncPref.setSummary(mSyncPref.getEntries()[index]);
                mSyncPref.setValue(summary);
                switch (index) {
                    case 0:
                        internal = 15000;
                        break;
                    case 1:
                        internal = 20000;
                        break;
                    case 2:
                        internal = 60000;
                        break;
                    case 3:
                        internal = 180000;
                        break;
                    case 4:
                        internal = 360000;
                        break;

                }
                if (null != sharedPreferencesHelper) {
                    /*if (null != App.timerTask) {
                        LogUtil.d("times == " + internal);
                        App.timerTask.setPeriod(internal);
                    }*/
                    sharedPreferencesHelper.putInt(SP_POST_INTERNAL, internal);
                }
                return true;
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.clear();
        menu.add(0, MENU_RESTORE_DEFAULTS, 0, R.string.restore_default);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerListeners();
        invalidateOptionsMenu();
    }

    private void registerListeners() {
        mNewMessagePref.setOnPreferenceChangeListener(this);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case MENU_RESTORE_DEFAULTS:
                restoreDefaultPreferences();
                return true;

            case android.R.id.home:
                // The user clicked on the Messaging icon in the action bar. Take them back from
                // wherever they came from
                finish();
                return true;
        }
        return false;
    }
    private void restoreDefaultPreferences() {
        PreferenceManager.getDefaultSharedPreferences(this).edit().clear().apply();
        setPreferenceScreen(null);

    }

    /**
     * Set up the {@link android.app.ActionBar}, if the API is available.
     */
    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private static UpdateInfo info;
    private AlertDialog mAlertDialog;

    //Add by yangli for UpgradeVersion
    /*
 * 从服务器获取xml解析并进行比对版本号
 */
    public class CheckVersionTask implements Runnable{

        public void run() {
            try {
                String versionname = UpdateUtil.getVersionName();
                LogUtil.d("versionname = " + versionname);
                //从资源文件获取服务器 地址,这个地址到时候填你们自己的APK更新服务器地址
                String path = UTT_UPGARADE_SETVER_URL;
                //包装成url的对象
                URL url = new URL(path);
                HttpURLConnection conn =  (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(5000);
                InputStream is =conn.getInputStream();
                info =  UpdateUtil.getUpdataInfo(is);

                if(info.getVersion().equals(versionname)){
                    LogUtil.d("版本号相同无需升级");
                    LoginMain();
                }else{
                    LogUtil.d("版本号不同 ,提示用户升级");
                    Message msg = new Message();
                    msg.what = MSG_UPDATA_CLIENT;
                    handler.sendMessage(msg);
                }
            } catch (Exception e) {
                // 待处理
                Message msg = new Message();
                msg.what = MSG_GET_UNDATAINFO_ERROR;
                handler.sendMessage(msg);
                e.printStackTrace();
            }
        }
    }

    Handler handler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_UPDATA_CLIENT:
                    //对话框通知用户升级程序
                    showUpdataDialog();
                    break;
                case MSG_GET_UNDATAINFO_ERROR:
                    //服务器超时
                    Toast.makeText(getApplicationContext(), R.string.upgrade_info_get_error, Toast.LENGTH_SHORT).show();
                    //LoginMain();
                    break;
                case MSG_DOWN_ERROR:
                    //下载apk失败
                    Toast.makeText(getApplicationContext(), R.string.download_apk_failed, Toast.LENGTH_SHORT).show();
                    //LoginMain();
                    break;
            }
        }
    };

    /*
 *
 * 弹出对话框通知用户更新程序
 *
 * 弹出对话框的步骤：
 *  1.创建alertDialog的builder.
 *  2.要给builder设置属性, 对话框的内容,样式,按钮
 *  3.通过builder 创建一个对话框
 *  4.对话框show()出来
 */
    protected void showUpdataDialog() {
        /*AlertDialog.Builder builer = new AlertDialog.Builder(this) ;
        builer.setTitle("版本升级");
        if (null != info) {
            builer.setMessage(info.getDescription());
        }
        //当点确定按钮时从服务器上下载 新的apk 然后安装
        builer.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                LogUtil.d("下载apk,更新");
                downLoadApk();
            }
        });
        //当点取消按钮时进行登录
        builer.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                LoginMain();
            }
        });
        AlertDialog dialog = builer.create();
        dialog.show();*/

        if (null != mAlertDialog) {
            mAlertDialog.dismiss();
            mAlertDialog = null;
        }
        String description = getString(R.string.defaultt_version_description);
        if (null != info) {
            description = info.getDescription();
        }
        AlertDialogCreator.getInstance().setmButtonOnClickListener(mDialogListener);
        mAlertDialog = AlertDialogCreator
                .getInstance()
                .createAlertDialogNormal(
                        pThis,
                        getString(R.string.upgrade_version),
                        description);
        mAlertDialog.show();
    }

    private AlertDialogCreator.ButtonOnClickListener mDialogListener = new AlertDialogCreator.ButtonOnClickListener() {
        @Override
        public void buttonTrue() {
            LogUtil.d("下载apk,更新");
            downLoadApk();
        }

        @Override
        public void buttonTrue(int ring_dis) {

        }

        @Override
        public void buttonTrue(String value) {

        }

        @Override
        public void buttonTrue(String valuekey, String name) {

        }

        @Override
        public void buttonCancel() {

            //ToastHelper.showAlert(mContext, getString(R.string.boolth_eable_tip));
            //finish();
        }
    };

    /*
     * 从服务器中下载APK
     */
    protected void downLoadApk() {
        final ProgressDialog pd;    //进度条对话框
        pd = new  ProgressDialog(this);
        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pd.setMessage("正在下载更新");
        pd.show();
        new Thread(){
            @Override
            public void run() {
                try {
                    File file = UpdateUtil.getFileFromServer(info.getUrl(), pd);
                    sleep(3000);
                    installApk(file);
                    pd.dismiss(); //结束掉进度条对话框
                } catch (Exception e) {
                    Message msg = new Message();
                    msg.what = MSG_DOWN_ERROR;
                    handler.sendMessage(msg);
                    e.printStackTrace();
                }
            }}.start();
    }

    //安装apk
    protected void installApk(File file) {
        Intent intent = new Intent();
        //执行动作
        intent.setAction(Intent.ACTION_VIEW);
        //执行的数据类型
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        startActivity(intent);
    }

    /*
     * 进入程序的主界面
     */
    private void LoginMain(){
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
        //结束掉当前的activity
        this.finish();
    }

}
