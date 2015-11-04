package com.original.abroadeasy;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioGroup;

import com.original.abroadeasy.util.LogUtil;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private BaseFragment mCurrentFragment;
    private static final String[] FRAGMENT_TAGS = {"home", "find", "search", "user"};
    private static final int ID_HOME = 0;
    private static final int ID_FIND = 1;
    private static final int ID_BLOG = 2;
    private static final int ID_USER = 3;

    @Bind(R.id.swipe_refresh_layout)
    SwipeRefreshLayout mSwipRefreshLayout;

    @Bind(R.id.fab)
    View mFloatingActionBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        RadioGroup rg = (RadioGroup) findViewById(R.id.tab_bar);
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkId) {
                switch (checkId) {
                    case R.id.tab_home:
                        switchToFragment(ID_HOME);
                        break;
                    case R.id.tab_find:
                        switchToFragment(ID_FIND);
                        break;
                    case R.id.tab_blog:
                        switchToFragment(ID_BLOG);
                        break;
                    case R.id.tab_user_info:
                        switchToFragment(ID_USER);
                        break;
                }
            }
        });
        mSwipRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mCurrentFragment.onRefresh();
            }
        });
        mSwipRefreshLayout.setColorSchemeColors(Color.RED, Color.YELLOW, Color.GREEN);
        switchToFragment(ID_HOME);
    }

    @OnClick(R.id.fab)
    public void onFabClick(View v) {
        LogUtil.d("FAB clicked!!");
        LogUtil.d("maxMemory():" + Runtime.getRuntime().maxMemory());
        LogUtil.d("totalMemory():" + Runtime.getRuntime().totalMemory());
    }

    private void switchToFragment(int id) {
        mCurrentFragment = (BaseFragment) getFragmentManager().findFragmentByTag(FRAGMENT_TAGS[id]);
        if (mCurrentFragment == null) {
            switch (id) {
                case ID_HOME:
                    mCurrentFragment = new HomeFragment();
                    mSwipRefreshLayout.setEnabled(true);
                    mFloatingActionBtn.setVisibility(View.VISIBLE);
                    break;
                case ID_FIND:
                    mCurrentFragment = new FindFragment();
                    break;
                case ID_BLOG:
                    mCurrentFragment = new BlogFragment();
                    mFloatingActionBtn.setVisibility(View.GONE);
                    break;
                case ID_USER:
                    mCurrentFragment = new UserInfoFragment();
                    mSwipRefreshLayout.setEnabled(false);
                    mFloatingActionBtn.setVisibility(View.GONE);
                    break;
            }
        }
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_layout, mCurrentFragment, FRAGMENT_TAGS[id]).commit();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        ButterKnife.unbind(this);
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
