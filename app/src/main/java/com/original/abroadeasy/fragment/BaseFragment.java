package com.original.abroadeasy.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.original.abroadeasy.R;

/**
 * Created by zengjinlong on 15-11-2.
 */
public class BaseFragment extends Fragment {

    protected Activity mActivity;
    SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = activity;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        mSwipeRefreshLayout = (SwipeRefreshLayout) mActivity.findViewById(R.id.swipe_refresh_layout);
        super.onViewCreated(view, savedInstanceState);
    }

    public boolean handleBackKey() {
        return false;
    }

    RecyclerView.OnScrollListener mRecylerViewScrollListener = new RecyclerView.OnScrollListener() {
        private int mScrolledY = 0;

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            mScrolledY += dy;
            if (mScrolledY > 0) {
                setSwipeEnable(false);
            } else {
                setSwipeEnable(true);
            }
        }
    };

    public void onFabClicked() {

    }

    public void onRefresh() {
        // implement by child who wants handle refresh.
    }
    public void setRefreshing(boolean refreshing) {
        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setRefreshing(refreshing);
        }
    }
    public void setSwipeEnable(boolean enable) {
        mSwipeRefreshLayout.setEnabled(enable);
    }
}
