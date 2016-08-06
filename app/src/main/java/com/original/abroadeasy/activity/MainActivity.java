package com.original.abroadeasy.activity;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.kf5sdk.init.KF5SDKConfig;
import com.original.abroadeasy.R;
import com.original.abroadeasy.fragment.BaseFragment;
import com.original.abroadeasy.fragment.BlogFragment;
import com.original.abroadeasy.fragment.ExploreFragment;
import com.original.abroadeasy.fragment.HomeFragment;
import com.original.abroadeasy.fragment.UserInfoFragment;
import com.original.abroadeasy.util.PreferenceUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener{

    private static final String TAG = "ABE_MainActivity";
    private BaseFragment mCurrentFragment;
    private static final String[] FRAGMENT_TAGS = {"home", "find", "search", "user"};
    private static final int ID_HOME = 0;
    private static final int ID_FIND = 1;
    private static final int ID_BLOG = 2;
    private static final int ID_USER = 3;
    private int mCrrrentFragmentId = ID_HOME;

    private EditText mSearchView;
    private View mVoiceSearchButton;

    //public static ActionBar actionBar;

    private String mSearchQuery;

    //private ActionBarController mActionBarController;

    @Bind(R.id.swipe_refresh_layout)
    SwipeRefreshLayout mSwipRefreshLayout;

    @Bind(R.id.fab)
    public android.support.design.widget.FloatingActionButton mFloatingActionBtn;

    /**
     * Open the search UI when the user clicks on the search box.
     */
    /*private final View.OnClickListener mSearchViewOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (!isInSearchUi()) {
                mActionBarController.onSearchBoxTapped();
                enterSearchUi(false *//* smartSearch *//*, mSearchView.getText().toString());
            }
        }
    };*/

    /**
     * Listener used to send search queries to the find search fragment.
     */
    private final TextWatcher mFindSearchQueryTextListener = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            final String newText = s.toString();
            if (newText.equals(mSearchQuery)) {
                // If the query hasn't changed (perhaps due to activity being destroyed
                // and restored, or user launching the same DIAL intent twice), then there is
                // no need to do anything here.
                return;
            }

        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };

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
                    /*case R.id.tab_user_info:
                        switchToFragment(ID_USER);
                        break;*/
                }
            }
        });
        mSwipRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mCurrentFragment.onRefresh();
            }
        });
        mSwipRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.ae_theme_color));
        switchToFragment(ID_HOME);
        if (PreferenceUtils.isFirstLaunch(this)) {
            Snackbar.make(mSwipRefreshLayout, R.string.tip_pull_to_refresh, Snackbar.LENGTH_INDEFINITE)
                    .setAction(R.string.tip_ok, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                        }
                    }).show();
        }

        //add by yangli
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, null, R.string.abc_action_bar_home_description, R.string.abc_action_bar_up_description);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        /*actionBar = getSupportActionBar();

        Log.d(TAG, "actionbar = " + actionBar);
        actionBar.setCustomView(R.layout.find_search_edittext);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setBackgroundDrawable(null);
        //hide it
        actionBar.hide();

        mActionBarController = new ActionBarController(this,
                (SearchEditTextLayout) actionBar.getCustomView());

        SearchEditTextLayout searchEditTextLayout =
                (SearchEditTextLayout) actionBar.getCustomView();
        searchEditTextLayout.setPreImeKeyListener(mSearchEditTextLayoutListener);

        mSearchView = (EditText) searchEditTextLayout.findViewById(R.id.search_view);
        mSearchView.addTextChangedListener(mFindSearchQueryTextListener);
        mVoiceSearchButton = searchEditTextLayout.findViewById(R.id.voice_search_button);
        searchEditTextLayout.findViewById(R.id.search_magnifying_glass)
                .setOnClickListener(mSearchViewOnClickListener);
        searchEditTextLayout.findViewById(R.id.search_box_start_search)
                .setOnClickListener(mSearchViewOnClickListener);
        searchEditTextLayout.setOnBackButtonClickedListener(new SearchEditTextLayout.OnBackButtonClickedListener() {
            @Override
            public void onBackButtonClicked() {
                onBackPressed();
            }
        });*/
        //add end by yangli
        //Add the animation in the startup by yangli 2013.11.11 happy singles day
        TranslateAnimation alphaAnimation = new TranslateAnimation(0, 0, 0,
                -70);
        alphaAnimation.setDuration(500);
        alphaAnimation.setRepeatCount(3);
        alphaAnimation.setRepeatMode(Animation.REVERSE);
        mFloatingActionBtn.setAnimation(alphaAnimation);
        alphaAnimation.start();
    }

    @OnClick(R.id.fab)
    public void onFabClick(View v) {
        if (mCrrrentFragmentId == ID_HOME) {
            KF5SDKConfig.INSTANCE.startFeedBackActivity(MainActivity.this);
        } else {
            mCurrentFragment.onFabClicked();
        }
    }

    @Override
    public void onBackPressed() {
        if (null!= mCurrentFragment && mCurrentFragment.handleBackKey()) {
            return;
        }

        //Add by yangli 20151224 MerryX to ZJL
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
        //Add end
        super.onBackPressed();
    }

    private void switchToFragment(int id) {
        mCurrentFragment = (BaseFragment) getFragmentManager().findFragmentByTag(FRAGMENT_TAGS[id]);
        mSwipRefreshLayout.setRefreshing(false);
        mCrrrentFragmentId = id;
        if (mCurrentFragment == null) {
            switch (id) {
                case ID_HOME:
                    mCurrentFragment = new HomeFragment();
                    mSwipRefreshLayout.setEnabled(true);
                    mFloatingActionBtn.setVisibility(View.VISIBLE);
                    mFloatingActionBtn.setImageResource(R.mipmap.add);
                    mFloatingActionBtn.setEnabled(true);
                    break;
                case ID_FIND:
                    mCurrentFragment = new ExploreFragment();
                    mSwipRefreshLayout.setEnabled(true);
                    mFloatingActionBtn.setVisibility(View.VISIBLE);
                    mFloatingActionBtn.setImageResource(R.mipmap.ic_ab_search);
                    mFloatingActionBtn.setEnabled(true);
                    break;
                case ID_BLOG:
                    mCurrentFragment = new BlogFragment();
                    mSwipRefreshLayout.setEnabled(true);
                    mFloatingActionBtn.setVisibility(View.GONE);
                    mFloatingActionBtn.setEnabled(false);
                    break;
                case ID_USER:
                    mCurrentFragment = new UserInfoFragment();
                    mSwipRefreshLayout.setEnabled(false);
                    mFloatingActionBtn.setVisibility(View.GONE);
                    mFloatingActionBtn.setEnabled(false);
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
        HomeFragment.mFirstLoad = true;
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /**
     * If the search term is empty and the user closes the soft keyboard, close the search UI.
     */
    /*private final View.OnKeyListener mSearchEditTextLayoutListener = new View.OnKeyListener() {
        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN &&
                    TextUtils.isEmpty(mSearchView.getText().toString())) {
                maybeExitSearchUi();
            }
            return false;
        }
    };*/

    /**
     * @return True if the search UI was exited, false otherwise
     */
    /*private boolean maybeExitSearchUi() {
        if (isInSearchUi() && TextUtils.isEmpty(mSearchQuery)) {
            //exitSearchUi();
            //DialerUtils.hideInputMethod(mParentLayout);
            return true;
        }
        return false;
    }*/

    /**
     * Shows the search fragment
     */
    /*private void enterSearchUi(boolean smartDialSearch, String query) {
        if (getFragmentManager().isDestroyed()) {
            // Weird race condition where fragment is doing work after the activity is destroyed
            // due to talkback being on (b/10209937). Just return since we can't do any
            // constructive here.
            return;
        }
    }*/

    @Override
    protected void onStart() {
        super.onStart();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camara) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {
            Intent intent = new Intent(this, SettingActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
