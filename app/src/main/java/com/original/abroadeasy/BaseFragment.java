package com.original.abroadeasy;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;

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

    public void onRefresh() {
        // implement by child who wants handle refresh.
    }
    public void setRefreshing(boolean refreshing) {
        mSwipeRefreshLayout.setRefreshing(refreshing);
    }
    public void setSwipeEnable(boolean enable) {
        mSwipeRefreshLayout.setEnabled(enable);
    }
}
