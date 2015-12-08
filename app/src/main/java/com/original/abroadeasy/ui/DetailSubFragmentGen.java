package com.original.abroadeasy.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.original.abroadeasy.R;
import com.original.abroadeasy.util.CustomerViewPager;
import com.original.abroadeasy.util.LogUtil;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by yangli on 15-12-03
 */
public class DetailSubFragmentGen extends Fragment {

    private View mView;

    @Bind(R.id.conent_webview)
    WebView mWebView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_sub_general, container, false);

        ButterKnife.bind(this, mView);
        return mView;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!mLoaded) {
            initWebView();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    private boolean mLoaded = false;

    CustomerViewPager mPager;
    public void setViewPager(CustomerViewPager pager) {
        mPager = pager;
    }
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
                if (newProgress == 100) {
                    mLoaded = true;
                } else {

                }
//                mWebView.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        mWebView.measure(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//                        mWebView.invalidate();
//                        LogUtil.d("mWebView:" + mWebView.getHeight());
//                        mPager.measure(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//                        mPager.invalidate();
//                        mPager.forceLayout();
//                        mPager.requestLayout();
//                    }
//                }, 50);

            }
        });
        mWebView.loadUrl("http://square.github.io/retrofit/");
    }

}
