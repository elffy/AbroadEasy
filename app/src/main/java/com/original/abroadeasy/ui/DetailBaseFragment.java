package com.original.abroadeasy.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ListView;

import com.original.abroadeasy.widget.ListenableListView;

/**
 * Created by zengjinlong on 15-11-2.
 */
public class DetailBaseFragment extends Fragment {

    protected Activity mActivity;
    protected ListenableListView mListView;

    protected ListenableListView.OnListScrollListener mListScrollListener;
    public void setListScrollListener(ListenableListView.OnListScrollListener l) {
        mListScrollListener = l;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = activity;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public ListView getListView() {
        return mListView;
    }
}
