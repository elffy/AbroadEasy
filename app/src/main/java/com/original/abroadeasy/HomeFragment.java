package com.original.abroadeasy;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class HomeFragment extends BaseFragment {

    private View mView;
    private RecyclerView mRecyclerView;
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
}
