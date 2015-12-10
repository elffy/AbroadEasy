package com.original.abroadeasy.ui;

import android.app.ActionBar;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.original.abroadeasy.R;
import com.original.abroadeasy.model.UserInfo;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by zengjinlong on 15-10-30.
 */
public class SettingActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_layout);
        ButterKnife.bind(this);
        ActionBar actionBar = getActionBar();
        //actionBar.setDisplayOptions(ActionBar.DISPLAY_HOME_AS_UP | ActionBar.DISPLAY_SHOW_TITLE);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home:
                this.finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @OnClick(R.id.clear_cache)
    void onClearCacheClick(View v){

    }

    @OnClick(R.id.log_out)
    void onLogOutClick(View v) {
        UserInfo.logOut(this);
    }
}
