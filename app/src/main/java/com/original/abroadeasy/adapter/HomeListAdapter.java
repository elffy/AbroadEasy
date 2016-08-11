package com.original.abroadeasy.adapter;

import android.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.original.abroadeasy.R;
import com.original.abroadeasy.model.HomeItem;
import com.original.abroadeasy.model.ProgramItem;
import com.original.abroadeasy.util.LogUtil;

import java.util.List;
import java.util.Random;

/**
 * Created by zengjinlong on 15-11-29.
 */
public class HomeListAdapter  extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int ITEM_TYPE_NORMAL = 0;
    private static final int ITME_TYPE_HEADER = 1;
    private Fragment mFragment;
    List<HomeItem> mData;
    final LayoutInflater mLayoutInflater;
    private OnItemClickListener mItemClickListener;
    private OnScrollListener mScrollListener;
    private RecyclerView.ViewHolder mHeaderViewHolder;

    private static int[] imags = {
            R.mipmap.test1,
            R.mipmap.test2,
            R.mipmap.test3,
            R.mipmap.test4,
            R.mipmap.test5,
            R.mipmap.test6,
            R.mipmap.test8,
            R.mipmap.test9,
            R.mipmap.test10,
            R.mipmap.test11,
            R.mipmap.test12,
    };


    private static Random rand = new Random(47);

    public interface OnItemClickListener {
        void onItemClicked(View view, int postion);
    }
    public interface OnScrollListener {
        void onScrollToEnd();
    }

    public HomeListAdapter(Fragment fragment, LayoutInflater layoutInflater, List<HomeItem> datas) {
        mFragment = fragment;
        mData = datas;
        mLayoutInflater = layoutInflater;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mItemClickListener = listener;
    }
    public void setScrollListener(OnScrollListener listener) {
        mScrollListener = listener;
    }

    public void setmHeaderViewHolder(RecyclerView.ViewHolder holder) {
        mHeaderViewHolder = holder;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return ITME_TYPE_HEADER;
        } else {
            return ITEM_TYPE_NORMAL;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        LogUtil.d("onCreateViewHolder:" + viewType);
        if (viewType == ITEM_TYPE_NORMAL) {
            return new MyViewHolder(mItemClickListener,
                    mLayoutInflater.inflate(R.layout.home_item_view, parent, false));
        } else {
            return mHeaderViewHolder;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        LogUtil.d("onBindViewHolder:" + position);
        if (position > 0) {
            if (position  == getItemCount() - 1 && mScrollListener != null) {
                mScrollListener.onScrollToEnd();
            }
            MyViewHolder myHolder = (MyViewHolder)holder;
            HomeItem program = mData.get(position-1);
            myHolder.bindTo(program, position);
            // TODO study about the cache strategy about Glide.
            /*Glide.with(mFragment)
                    .load(program.getImageUri())
                    .centerCrop()
                    .into(myHolder.mImage);*/
        }
    }

    @Override
    public int getItemCount() {
        if (mHeaderViewHolder != null) {
            return mData.size() + 1;
        }
        return mData.size();
    }

    private static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView mTitle;
        private TextView mLocation;
        private TextView mTime;
        public ImageView mImage;
        private int mPostion;
        private OnItemClickListener mOnItemClickListener;
        private HomeItem mBoundItem;
        private int size = imags.length;

        public MyViewHolder(OnItemClickListener listener, View itemView) {
            super(itemView);
            mOnItemClickListener = listener;
            mTitle = (TextView) itemView.findViewById(R.id.title);
            mImage = (ImageView) itemView.findViewById(R.id.item_image);
            mLocation = (TextView) itemView.findViewById(R.id.loaction);
            mTime = (TextView) itemView.findViewById(R.id.time);
            itemView.setOnClickListener(this);
        }

        public void bindTo(HomeItem item, int postion) {
            mBoundItem = item;
            mPostion = postion;
            mTitle.setText(item.getTitle());
            mLocation.setText(item.getDescription());
            mTime.setText(item.getAverage()/**/);
            int img = rand.nextInt(size);
            mImage.setImageResource(imags[img]);
        }

        @Override
        public void onClick(View view) {
            mOnItemClickListener.onItemClicked(view, mPostion);
        }
    }


}
