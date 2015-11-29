package com.original.abroadeasy.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.original.abroadeasy.R;
import com.original.abroadeasy.adapter.HomeListAdapter;
import com.original.abroadeasy.model.HomeItem;
import com.original.abroadeasy.widget.BannerGallery;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A placeholder fragment containing a simple view.
 */
public class HomeFragment extends BaseFragment {

    private View mView;

    @Bind(R.id.recycler_view)
    RecyclerView mRecyclerView;

    BannerGallery mBannerGallery;
    LinearLayout mDotLayout;
    FrameLayout mBannerLayout;

    private HeaderViewHolder mHeaderViewHolder;
    private MyHandler mHandler;

    public HomeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, mView);
        intiView();
        mHandler = new MyHandler();
        return mView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
        mHandler.removeCallbacksAndMessages(null);
    }

    @Override
    public void onRefresh() {
        mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_REFRESH, 0, 0), 1000);
    }

    private static final int MSG_REFRESH = 0;

    private class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            int what = msg.what;
            if (MSG_REFRESH == what) {
                if (msg.arg1 < 3) {
                    msg.arg1++;
                    sendMessageDelayed(mHandler.obtainMessage(MSG_REFRESH, msg.arg1 + 1, 0), 1000);
                } else {
                    setRefreshing(false);
                }
            }
        }
    }

    private LinearLayoutManager mLinearLayoutManager;
    private HomeListAdapter mAdapter;

    private void intiView() {

        // init banner first.
        initBanner();

        // init recyclerView.
        mLinearLayoutManager = new LinearLayoutManager(mActivity);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mAdapter = new HomeListAdapter(mActivity, mActivity.getLayoutInflater());
        mAdapter.setOnItemClickListener(new HomeListAdapter.OnItemClickListener() {
            @Override
            public void onItemClicked(View view, int postion) {
                startDetailActivity();
            }
        });
        mAdapter.setmHeaderViewHolder(mHeaderViewHolder);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            private int mScrolledY = 0;

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                mScrolledY += dy;
                if (mScrolledY > 0) {
                    setSwipeEnable(false);
                } else {
                    setSwipeEnable(true);
                }
            }
        });
    }

    private void startDetailActivity() {
        Intent intent = new Intent(mActivity, DetailActivity.class);
        startActivity(intent);
    }

    private static class HeaderViewHolder extends RecyclerView.ViewHolder {
        public HeaderViewHolder(View itemView) {
            super(itemView);
        }
    }

    private void initBanner() {
        mBannerLayout = (FrameLayout) mActivity.getLayoutInflater().inflate(R.layout.home_banner_layout, null);
        // somehow the layoutParams will lost after inflate...???
        mBannerLayout.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT));
        mHeaderViewHolder = new HeaderViewHolder(mBannerLayout);
        mBannerGallery = (BannerGallery) mBannerLayout.findViewById(R.id.banner_gallery);
        mDotLayout = (LinearLayout) mBannerLayout.findViewById(R.id.banner_dot);
        BannerAdapter adapter = new BannerAdapter(mActivity);
        mBannerGallery.setAdapter(adapter);
        mBannerGallery.setSelection(1000);
        mBannerGallery.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                startDetailActivity();
            }
        });
        mBannerGallery.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                pos = pos % sBannerCount;
                for (int i = 0; i < mDotLayout.getChildCount(); i++) {
                    if (pos == i) {
                        ((ImageView) mDotLayout.getChildAt(i)).setImageResource(R.mipmap.banner_dot_focus);
                    } else {
                        ((ImageView) mDotLayout.getChildAt(i)).setImageResource(R.mipmap.banner_dot_normal);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        for (int i = 0; i < sBannerCount; i++) {
            ImageView view = new ImageView(mActivity);
            view.setImageResource(R.mipmap.banner_dot_normal);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

            lp.leftMargin = 3;
            lp.rightMargin = 3;
            mDotLayout.addView(view, 0, lp);
        }
    }

    public static int sBannerCount;

    class BannerAdapter extends BaseAdapter {

        private Context mContext;
        public int[] mImages;

        BannerAdapter(Context context) {
            mContext = context;
            mImages = new int[]{R.mipmap.u36, R.mipmap.u38, R.mipmap.u40

            };
            sBannerCount = mImages.length;
        }


        @Override
        public int getCount() {
            return Integer.MAX_VALUE;
        }

        @Override
        public Object getItem(int pos) {
            return mImages[pos];
        }

        @Override
        public long getItemId(int pos) {
            return pos;
        }

        @Override
        public View getView(int pos, View convertView, ViewGroup parent) {
            if (null == convertView) {
                convertView = new ImageView(mContext);
                ViewGroup.LayoutParams layoutParams = new Gallery.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);
                convertView.setLayoutParams(layoutParams);
                ((ImageView) convertView).setScaleType(ImageView.ScaleType.CENTER_CROP);
            }


            pos = pos % sBannerCount;
            ((ImageView) convertView).setImageResource(mImages[pos]);
            return convertView;
        }
    }
}
