package com.original.abroadeasy.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.original.abroadeasy.R;
import com.original.abroadeasy.util.LogUtil;
import com.original.abroadeasy.widget.ListenableListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by zengjinlong on 15-11-2.
 */
public class DetailActivity extends BaseActivity {

    private static final String TAG = "ABE_DetailActivity";

    @Bind(R.id.cover_layout)
    View mCoverLayout;
    @Bind(R.id.detail_pic)
    ImageView mPicture;
    @Bind(R.id.middle_btns_layout)
    View mMiddleBars;
    @Bind(R.id.view_pager)
    ViewPager mViewPager;

    PagerAdapter mPagerAdapter;
    DetailBaseFragment mCurrentFragment;

    private static final int IMAGE_LAYOUT_HEIGHT = 690;// define in the dimens/
    private int mLastScrollY;
    ListenableListView.OnListScrollListener mOnListScrollListener = new ListenableListView.OnListScrollListener() {
        @Override
        public void onYScrolled(int scrollY) {
            if (Math.abs(mLastScrollY - scrollY) > 200) {
                // this is not normal case...
                return;
            }
            mLastScrollY = scrollY;
            LogUtil.d("onYscrolled:" + scrollY);
            if (scrollY > -IMAGE_LAYOUT_HEIGHT) {
                mCoverLayout.setTranslationY(scrollY);
                mPicture.setTranslationY(-scrollY/2);
            } else {
                mCoverLayout.setTranslationY(-IMAGE_LAYOUT_HEIGHT);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_activity);
        ButterKnife.bind(this);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                LogUtil.d("onPageScrolled, position:" + position + ",positionOffset:" + positionOffset);
            }

            @Override
            public void onPageSelected(int position) {
                ((ListenableListView)mCurrentFragment.getListView()).setScrollListener(null);
                mCurrentFragment = (DetailBaseFragment) mPagerAdapter.getItem(position);
                ListView listView = mCurrentFragment.getListView();
                ((ListenableListView)listView).setScrollListener(mOnListScrollListener);
                float scrolledY = mCoverLayout.getTranslationY();
                if (scrolledY != 0) {
                    listView.setSelectionFromTop(0, (int)scrolledY);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        DetailBaseFragment fragmentMain1 = new DetailFragmentMain();
        DetailBaseFragment fragmentMain2 = new DetailFragmentMain();
        DetailBaseFragment fragment3 = new DetailFragmentSimple();
        DetailBaseFragment fragment4 = new DetailFragmentSimple();
        mCurrentFragment = fragmentMain1;
        mCurrentFragment.setListScrollListener(mOnListScrollListener);
        mPagerAdapter = new PagerAdapter(getSupportFragmentManager(),
                fragmentMain1, fragmentMain2, fragment3, fragment4);
        mViewPager.setAdapter(mPagerAdapter);
    }

    private static class PagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragments;

        public PagerAdapter(FragmentManager fm, Fragment... fragments) {
            super(fm);

            mFragments = new ArrayList<Fragment>();
            for (Fragment fragment : fragments) {
                mFragments.add(fragment);
            }
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

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

    @OnClick(R.id.btn_heart)
    void onHeartBtnClicked(View v) {
    }

    @OnClick(R.id.btn_star)
    void onStarBtnClicked(View v) {
    }

    @OnClick(R.id.btn_more)
    void onMoreBtnClicked(View v) {
    }

    @OnClick(R.id.btn_question)
    void onQuestionBtnClicked(View v) {
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

    /*@OnClick(R.id.btn_back)
    void onBackClicked(View v) {
        finish();
    }*/

    @OnClick(R.id.appointment_fab)
    void onFabClick(View v) {
        LogUtil.d("FAB clicked!!");
    }

}
