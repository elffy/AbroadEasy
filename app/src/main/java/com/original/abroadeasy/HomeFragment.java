package com.original.abroadeasy;

import android.app.Activity;
import android.content.Context;
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
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.original.abroadeasy.widget.BannerGallery;

import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class HomeFragment extends BaseFragment {

    private View mView;
    private Context mContext;
    private RecyclerView mRecyclerView;
    private BannerGallery mBannerGallery;
    private LinearLayout mDotLayout;
    private MyHandler mHandler;

    public HomeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_main, container, false);
        intiView();
        mHandler = new MyHandler();
        return mView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mContext = activity;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
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
    private MyAdapter mAdapter;

    private void intiView() {

        mBannerGallery = (BannerGallery) mView.findViewById(R.id.banner_gallery);
        mDotLayout = (LinearLayout) mView.findViewById(R.id.banner_dot);
        initBanner();
        mRecyclerView = (RecyclerView) mView.findViewById(R.id.recycler_view);
        mLinearLayoutManager = new LinearLayoutManager(mActivity);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mAdapter = new MyAdapter(mActivity.getLayoutInflater());
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

    private class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {
        ArrayList<HomeItem> mData;
        final LayoutInflater mLayoutInflater;

        public MyAdapter(LayoutInflater layoutInflater) {
            mData = new ArrayList<HomeItem>();
            mLayoutInflater = layoutInflater;
            for (int i = 0; i < 20; i++) {
                Drawable d;
                if (i % 3 == 0) {
                    d = mActivity.getResources().getDrawable(R.mipmap.pic1);
                } else if (i % 3 == 1) {
                    d = mActivity.getResources().getDrawable(R.mipmap.pic2);
                } else {
                    d = mActivity.getResources().getDrawable(R.mipmap.pic3);
                }
                mData.add(new HomeItem("this is item : " + i, d));
            }
        }

        @Override
        public MyViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
            return new MyViewHolder(
                    mLayoutInflater.inflate(R.layout.home_item_view, parent, false));
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            holder.bindTo(mData.get(position));
        }

        @Override
        public int getItemCount() {
            return mData.size();
        }

    }

    private static class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView mText;
        private ImageView mImage;
        HomeItem mBoundItem;

        public MyViewHolder(View itemView) {
            super(itemView);
            mText = (TextView) itemView.findViewById(R.id.item_text);
            mImage = (ImageView) itemView.findViewById(R.id.item_image);
        }

        public void bindTo(HomeItem item) {
            mBoundItem = item;
            mText.setText(item.mText);
            mImage.setImageDrawable(item.mPicture);
        }

    }

    private static class HomeItem {
        String mText;
        Drawable mPicture;
        private static int idCounter = 0;

        public HomeItem(String text, Drawable d) {
            mText = text;
            mPicture = d;
        }
    }

    private void initBanner() {
        BannerAdapter adapter = new BannerAdapter(mContext);
        mBannerGallery.setAdapter(adapter);
        mBannerGallery.setSelection(1000);
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
            ImageView view = new ImageView(mContext);
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
