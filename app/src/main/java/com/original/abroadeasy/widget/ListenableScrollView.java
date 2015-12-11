package com.original.abroadeasy.widget;

import android.content.Context;
import android.util.AttributeSet;

import com.original.abroadeasy.util.LogUtil;

/**
 * Created by zengjinlong on 15-12-2.
 */
public class ListenableScrollView extends android.support.v4.widget.NestedScrollView {

    public interface OnScrollChangedListener {
        void onScrollChanged(int x, int y, int oldx, int oldy);
    }
    private OnScrollChangedListener mOnScrollListener;

    public void setOnScrollListener(OnScrollChangedListener listener) {
        mOnScrollListener = listener;
    }

    public ListenableScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public ListenableScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ListenableScrollView(Context context) {
        super(context);
    }

    @Override
    protected void onOverScrolled(int scrollX, int scrollY, boolean clampedX, boolean clampedY) {
        super.onOverScrolled(scrollX, scrollY, clampedX, clampedY);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (mOnScrollListener != null) {
            mOnScrollListener.onScrollChanged(l, t, oldl, oldt);
        }
        LogUtil.d("onScrollChanged:" + l + "," + t + ",old:" + oldl + "," + oldt);
    }
}
