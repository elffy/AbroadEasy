package com.original.abroadeasy.ui;

import android.app.FragmentTransaction;
import android.graphics.Color;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.original.abroadeasy.R;
import com.original.abroadeasy.util.LogUtil;
import com.original.abroadeasy.util.PreferenceUtils;
import com.original.abroadeasy.util.Utils;
import com.original.abroadeasy.widget.ActionBarController;
import com.original.abroadeasy.widget.SearchEditTextLayout;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity implements ActionBarController.ActivityUi{

    private static final String TAG = "ABE_MainActivity";
    private BaseFragment mCurrentFragment;
    private static final String[] FRAGMENT_TAGS = {"home", "find", "search", "user"};
    private static final int ID_HOME = 0;
    private static final int ID_FIND = 1;
    private static final int ID_BLOG = 2;
    private static final int ID_USER = 3;
    private EditText mSearchView;
    private View mVoiceSearchButton;

    public static ActionBar actionBar;

    private String mSearchQuery;

    private ActionBarController mActionBarController;

    @Bind(R.id.swipe_refresh_layout)
    SwipeRefreshLayout mSwipRefreshLayout;

    @Bind(R.id.fab)
    View mFloatingActionBtn;

    /**
     * Open the search UI when the user clicks on the search box.
     */
    private final View.OnClickListener mSearchViewOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (!isInSearchUi()) {
                mActionBarController.onSearchBoxTapped();
                enterSearchUi(false /* smartSearch */, mSearchView.getText().toString());
            }
        }
    };

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
                        if (null != actionBar && true == actionBar.isShowing()) {
                            actionBar.hide();
                        }
                        switchToFragment(ID_HOME);
                        break;
                    case R.id.tab_find:
                        if (null != actionBar && false == actionBar.isShowing()) {
                            actionBar.show();
                        }
                        switchToFragment(ID_FIND);
                        break;
                    case R.id.tab_blog:
                        if (null != actionBar && true == actionBar.isShowing()) {
                            actionBar.hide();
                        }
                        switchToFragment(ID_BLOG);
                        break;
                    case R.id.tab_user_info:
                        if (null != actionBar && true == actionBar.isShowing()) {
                            actionBar.hide();
                        }
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
        if (PreferenceUtils.isFirstLaunch(this)) {
            Snackbar.make(mSwipRefreshLayout, R.string.tip_pull_to_refresh, Snackbar.LENGTH_INDEFINITE)
                    .setAction(R.string.tip_ok, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                        }
                    }).show();
        }

        //add by yangli
        actionBar = getSupportActionBar();

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
        });
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

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
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
                    mFloatingActionBtn.setVisibility(View.VISIBLE);
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

    /**
     * If the search term is empty and the user closes the soft keyboard, close the search UI.
     */
    private final View.OnKeyListener mSearchEditTextLayoutListener = new View.OnKeyListener() {
        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN &&
                    TextUtils.isEmpty(mSearchView.getText().toString())) {
                maybeExitSearchUi();
            }
            return false;
        }
    };

    /**
     * @return True if the search UI was exited, false otherwise
     */
    private boolean maybeExitSearchUi() {
        if (isInSearchUi() && TextUtils.isEmpty(mSearchQuery)) {
            //exitSearchUi();
            //DialerUtils.hideInputMethod(mParentLayout);
            return true;
        }
        return false;
    }

    @Override
    public boolean isInSearchUi() {
        return false;
    }

    @Override
    public boolean hasSearchQuery() {
        return false;
    }

    @Override
    public boolean shouldShowActionBar() {
        return false;
    }

    @Override
    public int getActionBarHeight() {
        return 0;
    }

    /**
     * Shows the search fragment
     */
    private void enterSearchUi(boolean smartDialSearch, String query) {
        if (getFragmentManager().isDestroyed()) {
            // Weird race condition where fragment is doing work after the activity is destroyed
            // due to talkback being on (b/10209937). Just return since we can't do any
            // constructive here.
            return;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
}
