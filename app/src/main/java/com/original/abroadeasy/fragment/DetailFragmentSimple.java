package com.original.abroadeasy.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.TextView;

import com.original.abroadeasy.R;
import com.original.abroadeasy.adapter.DetailFragmentAdapter;
import com.original.abroadeasy.app.App;
import com.original.abroadeasy.activity.DetailActivity;
import com.original.abroadeasy.widget.ListenableListView;

import butterknife.ButterKnife;

/**
 * Created by zengjinlong on 15-11-2.
 */
public class DetailFragmentSimple extends DetailBaseFragment {

    private View mView;
    private View mContentView;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.list_layout, container, false);
        ButterKnife.bind(this, mView);
        mListView = (ListenableListView) mView.findViewById(R.id.list);
        DetailFragmentAdapter adapter = new DetailFragmentAdapter(mActivity);
        if (mFragmentTag == DetailActivity.FRAGMENT_TAG_DETAIL) {
            mContentView = inflater.inflate(R.layout.detail_detail_layout, null);
            mContentView.setMinimumHeight(App.sScreenHeight);
            adapter.addView(mContentView);
        } else if (mFragmentTag == DetailActivity.FRAGMENT_TAG_PRICE) {
            mContentView = inflater.inflate(R.layout.detail_price_layout, null);
            mContentView.setMinimumHeight(App.sScreenHeight);
            adapter.addView(mContentView);
        } else {
            TextView textView = new TextView(container.getContext());
            textView.setAutoLinkMask(Linkify.ALL);
            textView.setText("Do you have any questions, visit our website : http://www.abroadeasy.com");
            textView.setPadding(48, 24, 24, 24);
            textView.setLayoutParams(new AbsListView.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, App.sScreenHeight));
            adapter.addView(textView);
        }
        mListView.initHeaderAndFooterView();
        mListView.setAdapter(adapter);
        return mView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

}
