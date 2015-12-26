package com.original.abroadeasy.ui;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.OnScrollListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.original.abroadeasy.R;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by yangli on 15-11-19 tiny lucky.
 */
public class BlogFragment extends BaseFragment {

    private View mView;

    @Bind(R.id.recycler_view_blog)
    RecyclerView mRecyclerView;

    ImageView mTitleImage;

    private BlogHandler mBlogHandler;

    public BlogFragment () {

    }




    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_blog, container, false);
        ButterKnife.bind(this, mView);
        initView();
        mBlogHandler = new BlogHandler();
        return mView;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
        mBlogHandler.removeCallbacksAndMessages(null);
    }

    @Override
    public void onRefresh() {
        super.onRefresh();
        mBlogHandler.sendMessageDelayed(mBlogHandler.obtainMessage(MSG_REFRESH, 0, 0), 1000);
    }

    private static final int MSG_REFRESH = 0;
    private class BlogHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            int what = msg.what;
            if (MSG_REFRESH == what) {
                if (msg.arg1 < 3) {
                    msg.arg1++;
                    sendMessageDelayed(mBlogHandler.obtainMessage(MSG_REFRESH, msg.arg1 + 1, 0), 1000);
                } else {
                    setRefreshing(false);
                }
            }
        }
    }

    private LinearLayoutManager mLinearLayoutManager;
    private BlogAdapter mBlogAdapter;
    private void initView() {

        mLinearLayoutManager = new LinearLayoutManager(mActivity);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mBlogAdapter = new BlogAdapter(mActivity.getLayoutInflater());
        mRecyclerView.setAdapter(mBlogAdapter);

        //if do nothing recyllerview wil refresh everywhere
        mRecyclerView.addOnScrollListener(new OnScrollListener() {
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


    private class BlogAdapter extends RecyclerView.Adapter<MyViewHolder> {
        ArrayList<BlogItem> mData;
        final LayoutInflater mLayoutInflater;
        public BlogAdapter(LayoutInflater layoutInflater) {
            mData = new ArrayList<BlogItem>();
            mLayoutInflater = layoutInflater;
            for (int i = 0; i < 20; i++) {
                Drawable d;
                if (i % 3 == 0) {
                    d = mActivity.getResources().getDrawable(R.mipmap.blog_im1);
                } else if (i % 3 == 1) {
                    d = mActivity.getResources().getDrawable(R.mipmap.blog_im2);
                } else {
                    d = mActivity.getResources().getDrawable(R.mipmap.blog_im3);
                }
                mData.add(new BlogItem("Top 10 Australian Beaches " + i, "Number" + i + "\n" +"Whitehaven Beach\n" +
                        "Whitsunday lsland,Whitsunday lslands ", d));
            }
        }

        @Override
        public MyViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
            return new MyViewHolder(
                    mLayoutInflater.inflate(R.layout.blog_item_view, parent, false));
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
        private TextView mTitleTv;
        private TextView mDescripTv;
        private ImageView mImage;
        BlogItem mBoundItem;
        public MyViewHolder(View itemView) {
            super(itemView);
            mTitleTv = (TextView) itemView.findViewById(R.id.tx_title_blog);
            mDescripTv = (TextView) itemView.findViewById(R.id.tx_descrip_blog);
            mImage = (ImageView) itemView.findViewById(R.id.image_blog_item);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //ToDo
                }
            });
        }

        public void bindTo(BlogItem item) {
            mBoundItem = item;
            mTitleTv.setText(item.mTitle);
            mDescripTv.setText(item.mDescrip);
            mImage.setImageDrawable(item.mPicture);
        }

    }

    private static class BlogItem {
        String mTitle;
        String mDescrip;
        Drawable mPicture;
        private static int idCounter = 0;

        public BlogItem(String title, String descrip, Drawable d) {
            mTitle = title;
            mDescrip = descrip;
            mPicture = d;
        }
    }
}
