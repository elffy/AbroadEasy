package com.original.abroadeasy.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AbsListView;
import android.widget.TextView;

import com.original.abroadeasy.R;
import com.original.abroadeasy.adapter.DetailFragmentAdapter;
import com.original.abroadeasy.app.App;
import com.original.abroadeasy.util.LogUtil;
import com.original.abroadeasy.widget.ListenableListView;

import butterknife.ButterKnife;

/**
 * Created by zengjinlong on 15-11-2.
 */
public class DetailFragmentSimple extends DetailBaseFragment {

    private View mView;

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
        TextView textView = new TextView(container.getContext());
        textView.setText("aaaaaaaaaaaaaaaaa/nbbbbbbbbbbbbbbbbbbbbbb/nccccccccccccccc");
        textView.setLayoutParams(new AbsListView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, App.sScreenHeight));
        adapter.addView(textView);
        mListView.initHeaderView();
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
