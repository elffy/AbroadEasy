package com.original.abroadeasy.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.ScrollView;

import com.original.abroadeasy.util.LogUtil;

/**
 * Created by zengjinlong on 15-12-2.
 */
public class ListenableScrollView extends ScrollView {

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
        mGestureDetector = new GestureDetector(new YScrollDetector());
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

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return super.onInterceptTouchEvent(ev) && mGestureDetector.onTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return super.onTouchEvent(ev);
    }

    private GestureDetector mGestureDetector;
    class YScrollDetector extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            return (Math.abs(distanceY) > Math.abs(distanceX));
        }
    }
}
