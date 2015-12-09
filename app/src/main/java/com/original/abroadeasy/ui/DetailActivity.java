package com.original.abroadeasy.ui;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.Toolbar;

import com.original.abroadeasy.R;
import com.original.abroadeasy.util.CustomerViewPager;
import com.original.abroadeasy.util.LogUtil;
import com.original.abroadeasy.util.ViewPagerTabs;
import com.original.abroadeasy.widget.ListenableScrollView;

import java.util.Objects;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by zengjinlong on 15-11-2.
 */
public class DetailActivity extends BaseActivity {

    private static final String TAG = "ABE_DetailActivity";


    @Bind(R.id.scroll_view)
    ListenableScrollView mScrollView;
    @Bind(R.id.detail_pic)
    ImageView mPicture;
    @Bind(R.id.middle_btns_layout)
    View mMiddleBars;

    //Add by yangli for viewtab
    //** ViewPager for swipe */
    private CustomerViewPager mTabPager;
    private ViewPagerTabs mViewPagerTabs;
    private TabPagerAdapter mTabPagerAdapter;
    private String[] mTabTitles;

    public static final int FRAGMENT_GENERAL = 0;
    public static final int FRAGMENT_CONTENT = 1;
    public static final int FRAGMENT_FARE = 2;
    public static final int FRAGMENT_COMMENT = 3;
    public int CURRENT_FRAGMENT_INDEX = 0;

    private final TabPagerListener mTabPagerListener = new TabPagerListener();
    private static ViewPagerTabs portraitViewPagerTabs = null;

    private DetailSubFragmentGen mDetailSubFragmentGen;
    private DetailSubFragmentContent mDetailSubFragmentContent;
    private DetailSubFragmentFare mDetailSubFragmentFare;
    private DetailSubFragmentComment mDetailSubFragmentComment;

    private static Resources res;
    private Toolbar toolbar;
    //Add end by yangli for viewtab

    private static final int IMAGE_LAYOUT_HEIGHT = 690;// define in the dimens/
    ListenableScrollView.OnScrollChangedListener mOnScrollListener = new ListenableScrollView.OnScrollChangedListener() {
        @Override
        public void onScrollChanged(int x, int y, int oldx, int oldy) {

            if (y < IMAGE_LAYOUT_HEIGHT) {
                mPicture.setTranslationY(-y/2);
                mMiddleBars.setTranslationY(-y);
            } else {
                mMiddleBars.setTranslationY(-IMAGE_LAYOUT_HEIGHT);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createViewsAndFragments(savedInstanceState);
        toolbar.setVisibility(View.GONE);

        ButterKnife.bind(this);

        mScrollView.setOnScrollListener(mOnScrollListener);
    }

    //Add by yangli 2015.12.03
    private void createViewsAndFragments(Bundle savedInstanceState) {
        setContentView(R.layout.detail_activity);

        final FragmentManager fragmentManager = getFragmentManager();
        //Hide all tabs
        final FragmentTransaction transaction = fragmentManager.beginTransaction();
        mTabTitles = new String[TabState.COUNT];
        mTabTitles[0] = getString(R.string.tab_title_gen);
        mTabTitles[1] = getString(R.string.tab_title_content);
        mTabTitles[2] = getString(R.string.tab_title_fare);
        mTabTitles[3] = getString(R.string.tab_title_comment);

        mTabPager = getView(R.id.tab_pager);
        mTabPagerAdapter = new TabPagerAdapter();
        mTabPager.setAdapter(mTabPagerAdapter);
        mTabPager.setOnPageChangeListener(mTabPagerListener);
        //init toolbar(Old action bar)
        toolbar = (Toolbar) this.findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        this.setActionBar(toolbar);

        portraitViewPagerTabs = (ViewPagerTabs) findViewById(R.id.lists_pager_header);
        ViewPagerTabs landscapeViewPagerTabs = null;
        if (null == portraitViewPagerTabs) {
            landscapeViewPagerTabs = (ViewPagerTabs) getLayoutInflater()
                    .inflate(R.layout.detail_activity_tabs_lands, toolbar, false);
            mViewPagerTabs = landscapeViewPagerTabs;
        } else {
            mViewPagerTabs = portraitViewPagerTabs;
        }
        mViewPagerTabs.setViewPager(mTabPager);

        final String GENERAL_TAG = "tag-pager-general";
        final String CONTENT_TAG = "tag-pager-content";
        final String FARE_TAG = "tag-pager-fare";
        final String COMMENT_TAG = "tag-pager-comment";
        // Create the fragments and add as children of the view pager.
        // The pager adapter will only change the visibility; it'll never
        // create/destroy
        // fragments.
        // However, if it's after screen rotation, the fragments have been
        // re-created by
        // the fragment manager, so first see if there're already the target
        // fragments
        // existing.


        mDetailSubFragmentGen = (DetailSubFragmentGen)fragmentManager.findFragmentByTag(GENERAL_TAG);
        mDetailSubFragmentContent = (DetailSubFragmentContent)fragmentManager.findFragmentByTag(CONTENT_TAG);
        mDetailSubFragmentFare = (DetailSubFragmentFare)fragmentManager.findFragmentByTag(FARE_TAG);
        mDetailSubFragmentComment = (DetailSubFragmentComment)fragmentManager.findFragmentByTag(COMMENT_TAG);

        if (null == mDetailSubFragmentGen) {
            mDetailSubFragmentGen = new DetailSubFragmentGen();
            mDetailSubFragmentContent = new DetailSubFragmentContent();
            mDetailSubFragmentFare = new DetailSubFragmentFare();
            mDetailSubFragmentComment = new DetailSubFragmentComment();
            transaction.add(R.id.tab_pager, mDetailSubFragmentGen, GENERAL_TAG);
            transaction.add(R.id.tab_pager, mDetailSubFragmentContent, COMMENT_TAG);
            transaction.add(R.id.tab_pager, mDetailSubFragmentFare, FARE_TAG);
            transaction.add(R.id.tab_pager, mDetailSubFragmentComment, COMMENT_TAG);
        }

        // Hide all fragments for now. We adjust visibility when we get
        // onSelectedTabChanged()
        // from ActionBarAdapter.
        transaction.hide(mDetailSubFragmentGen);
        transaction.hide(mDetailSubFragmentContent);
        transaction.hide(mDetailSubFragmentFare);
        transaction.hide(mDetailSubFragmentComment);

        transaction.commitAllowingStateLoss();
        fragmentManager.executePendingTransactions();


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }


    @Override
    protected void onPause() {
        super.onPause();
    }

    @OnClick(R.id.btn_setting)
    void onSettingBtnClicked(View v) {
    }

    @OnClick(R.id.btn_phone)
    void onPhoneBtnClicked(View v) {
    }

    @OnClick(R.id.btn_message)
    void onMessageBtnClicked(View v) {
    }

    @OnClick(R.id.btn_back)
    void onBackClicked(View v) {
        finish();
    }

    @OnClick(R.id.appointment_fab)
    void onFabClick(View v) {
        LogUtil.d("FAB clicked!!");
    }

    //Add by yangli for viewtab
    public interface TabState {
        public static int GENERAL = 0;
        public static int DETAIL = 1;
        public static int FARE = 2;
        public static int COMMENT = 3;
        public static int COUNT = 4;
        public static int DEFAULT = 1;
    }

    private class TabPagerListener implements ViewPager.OnPageChangeListener {
        public TabPagerListener() {
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            mViewPagerTabs.onPageScrolled(position, positionOffset, positionOffsetPixels);
        }

        @Override
        public void onPageSelected(int position) {

            CURRENT_FRAGMENT_INDEX = position;
            mViewPagerTabs.onPageSelected(position);
            invalidateOptionsMenu();
        }

        @Override
        public void onPageScrollStateChanged(int state) {

            mViewPagerTabs.onPageScrollStateChanged(state);
        }
    }

    private class TabPagerAdapter extends PagerAdapter {
        private final FragmentManager mFragmentManager;

        private FragmentTransaction mCurTransaction = null;

        private Fragment mCurrentPrimaryItem;

        public TabPagerAdapter() {
            mFragmentManager = getFragmentManager();
        }

        @Override
        public int getCount() {
            return TabState.COUNT;
        }

        @Override
        public int getItemPosition(Object object) {
            if (object == mDetailSubFragmentGen) {
                return 0;
            } else if (object == mDetailSubFragmentContent) {
                return 1;
            } else if (object == mDetailSubFragmentFare) {
                return 2;
            } else if (object == mDetailSubFragmentComment) {
                return 3;
            }
            return POSITION_NONE;
        }

        @Override
        public void startUpdate(ViewGroup container) {
            super.startUpdate(container);
        }

        private Fragment getFragment(int pos) {
            if (FRAGMENT_CONTENT == pos) {
                if (null == mDetailSubFragmentContent) {
                    mDetailSubFragmentContent = new DetailSubFragmentContent();
                }
                return mDetailSubFragmentContent;
            } else if (FRAGMENT_FARE == pos) {
                if (null == mDetailSubFragmentFare) {
                    mDetailSubFragmentFare = new DetailSubFragmentFare();
                }
                return mDetailSubFragmentFare;
            } else if (FRAGMENT_COMMENT == pos) {
                if (null == mDetailSubFragmentComment) {
                    mDetailSubFragmentComment = new DetailSubFragmentComment();
                }
                return mDetailSubFragmentComment;
            } else {
                if (null == mDetailSubFragmentGen) {
                    mDetailSubFragmentGen = new DetailSubFragmentGen();
                }
                return mDetailSubFragmentGen;
            }
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            if (null == mCurTransaction) {
                mCurTransaction = mFragmentManager.beginTransaction();
            }
            Fragment fragment = getFragment(position);
            mCurTransaction.show(fragment);

            fragment.setUserVisibleHint(fragment == mCurrentPrimaryItem);
            return fragment;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            if (null == mCurTransaction) {
                mCurTransaction = mFragmentManager.beginTransaction();
            }
            mCurTransaction.hide((Fragment) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return ((Fragment)object).getView() == view;
        }

        @Override
        public void finishUpdate(ViewGroup container) {
            if (null != mCurTransaction) {
                mCurTransaction.commitAllowingStateLoss();
                mCurTransaction = null;
                mFragmentManager.executePendingTransactions();
            }
        }

        @Override
        public void setPrimaryItem(ViewGroup container, int position, Object object) {
            Fragment fragment = (Fragment)object;
            if (fragment != mCurrentPrimaryItem) {
                if (null != mCurrentPrimaryItem) {
                    mCurrentPrimaryItem.setUserVisibleHint(false);
                }
                if (null != fragment) {
                    fragment.setUserVisibleHint(true);
                }
                mCurrentPrimaryItem= fragment;
            }
        }

        @Override
        public Parcelable saveState() {
            return null;
        }

        @Override
        public void restoreState(Parcelable state, ClassLoader loader) {

        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTabTitles[position];
        }
    }

    /**
     * Convenient version of {@link #findViewById(int)}, which throws
     * an exception if the view doesn't exist.
     */
    @SuppressWarnings("unchecked")
    public <T extends View> T getView(int id) {
        T result = (T)findViewById(id);
        if (result == null) {
            throw new IllegalArgumentException("view 0x" + Integer.toHexString(id)
                    + " doesn't exist");
        }
        return result;
    }

    protected static void showFragment(FragmentTransaction ft, Fragment f) {
        if ((f != null) && f.isHidden()) ft.show(f);
    }

    protected static void hideFragment(FragmentTransaction ft, Fragment f) {
        if ((f != null) && !f.isHidden()) ft.hide(f);
    }
    //Add end by yangli for viewtab

}
