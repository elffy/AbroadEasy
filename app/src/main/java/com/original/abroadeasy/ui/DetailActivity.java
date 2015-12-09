package com.original.abroadeasy.ui;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import com.original.abroadeasy.R;
import com.original.abroadeasy.util.LogUtil;
import com.original.abroadeasy.widget.ListenableScrollView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by zengjinlong on 15-11-2.
 */
public class DetailActivity extends BaseActivity {

    private static final String TAG = "ABE_DetailActivity";

    @Bind(R.id.conent_webview)
    WebView mWebView;
    @Bind(R.id.scroll_view)
    ListenableScrollView mScrollView;
    @Bind(R.id.detail_pic)
    ImageView mPicture;
    @Bind(R.id.middle_btns_layout)
    View mMiddleBars;

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
        setContentView(R.layout.detail_activity);
        ButterKnife.bind(this);

        mScrollView.setOnScrollListener(mOnScrollListener);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!mLoaded) {
            initWebView();
        }
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

}
