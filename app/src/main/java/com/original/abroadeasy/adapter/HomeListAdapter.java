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
import com.original.abroadeasy.model.ProgramItem;
import com.original.abroadeasy.util.LogUtil;

import java.util.List;

/**
 * Created by zengjinlong on 15-11-29.
 */
public class HomeListAdapter  extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int ITEM_TYPE_NORMAL = 0;
    private static final int ITME_TYPE_HEADER = 1;
    private Fragment mFragment;
    List<ProgramItem> mData;
    final LayoutInflater mLayoutInflater;
    private OnItemClickListener mItemClickListener;
    private OnScrollListener mScrollListener;
    private RecyclerView.ViewHolder mHeaderViewHolder;

    public interface OnItemClickListener {
        void onItemClicked(View view, int postion);
    }
    public interface OnScrollListener {
        void onScrollToEnd();
    }

    public HomeListAdapter(Fragment fragment, LayoutInflater layoutInflater, List<ProgramItem> datas) {
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
            ProgramItem program = mData.get(position - 1);
            myHolder.bindTo(program, position);
            // TODO study about the cache strategy about Glide.
            Glide.with(mFragment)
                    .load(program.image)
                    .centerCrop()
                    .into(myHolder.mImage);
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

        //private TextView mText;
        public ImageView mImage;
        private int mPostion;
        private OnItemClickListener mOnItemClickListener;
        private ProgramItem mBoundItem;

        public MyViewHolder(OnItemClickListener listener, View itemView) {
            super(itemView);
            mOnItemClickListener = listener;
            //mText = (TextView) itemView.findViewById(R.id.item_text);
            mImage = (ImageView) itemView.findViewById(R.id.item_image);
            itemView.setOnClickListener(this);
        }

        public void bindTo(ProgramItem item, int postion) {
            mBoundItem = item;
            mPostion = postion;
            //mText.setText(item.name);
            //mImage.setImageDrawable(item.mPicture);
        }

        @Override
        public void onClick(View view) {
            mOnItemClickListener.onItemClicked(view, mPostion);
        }
    }


}
