package com.original.abroadeasy;

import android.os.Bundle;

/**
 * Created by zengjinlong on 15-10-30.
 */
public class SettingActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_layout);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    // TODO
    private void clearCache(){

    }
}
