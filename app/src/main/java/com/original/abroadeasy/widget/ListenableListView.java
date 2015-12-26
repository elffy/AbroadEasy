package com.original.abroadeasy.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;

/**
 * Created by zengjinlong on 15-12-15.
 */
public class ListenableListView extends ListView implements AbsListView.OnScrollListener {

    public static final int HEAD_VIEW_HEIGHT = 243 * 3;//same as the cover layout
    public static final int FOOTER_VIEW_HEIGHT = 48 * 3;//same as the bottom bar

    public interface OnListScrollListener {
        void onYScrolled(int scrollY);
    }

    private View mHeadView;
    private OnListScrollListener mScrollListener;

    public void setScrollListener(OnListScrollListener l) {
        mScrollListener = l;
    }

    public ListenableListView(Context context) {
        super(context);
    }

    public ListenableListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOnScrollListener(this);
    }

    public void initHeaderAndFooterView() {
        mHeadView = new View(this.getContext());
        AbsListView.LayoutParams lp = new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, HEAD_VIEW_HEIGHT);
        mHeadView.setLayoutParams(lp);
        addHeaderView(mHeadView);
        View footerView  = new View(this.getContext());
        lp = new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, FOOTER_VIEW_HEIGHT);
        footerView.setLayoutParams(lp);
        addFooterView(footerView);
    }

    @Override
    public void onScrollStateChanged(AbsListView absListView, int i) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
                         int totalItemCount) {
        if (mScrollListener != null) {
            int y = mHeadView.getTop();
            mScrollListener.onYScrolled(y);
        }
    }
}
