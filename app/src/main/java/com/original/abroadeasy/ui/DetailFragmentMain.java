package com.original.abroadeasy.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.original.abroadeasy.R;
import com.original.abroadeasy.adapter.DetailFragmentAdapter;
import com.original.abroadeasy.app.App;
import com.original.abroadeasy.util.LogUtil;
import com.original.abroadeasy.widget.ListenableListView;

import butterknife.ButterKnife;

/**
 * Created by zengjinlong on 15-11-2.
 */
public class DetailFragmentMain extends DetailBaseFragment {

    private View mView;
    private WebView mWebView;
    private static boolean mIsFirstCreate = true;

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
        View contentView = inflater.inflate(R.layout.detail_main_layout, null);
        contentView.setMinimumHeight(App.sScreenHeight);
        mWebView = (WebView) contentView.findViewById(R.id.webview);
        DetailFragmentAdapter adapter = new DetailFragmentAdapter(mActivity);
        adapter.addView(contentView);
        mListView.initHeaderView();
        mListView.setAdapter(adapter);
        if (mIsFirstCreate) {
            mListView.setScrollListener(mListScrollListener);
        }
        return mView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        initWebView();
    }

    private boolean mLoaded = false;
    private void initWebView() {
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                LogUtil.d("onProgressChanged:" + newProgress);
                // TODO Auto-generated method stub
                if (newProgress == 100) {
                    mLoaded = true;
                } else {

                }

            }
        });
        mWebView.loadUrl("http://square.github.io/retrofit/");
    }

}
