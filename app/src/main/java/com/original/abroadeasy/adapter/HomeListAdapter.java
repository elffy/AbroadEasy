package com.original.abroadeasy.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.original.abroadeasy.R;

import java.util.ArrayList;

/**
 * Created by zengjinlong on 15-11-29.
 */
public class HomeListAdapter  extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int ITEM_TYPE_NORMAL = 0;
    private static final int ITME_TYPE_HEADER = 1;
    private Context mContext;
    ArrayList<HomeItem> mData;
    final LayoutInflater mLayoutInflater;
    private OnItemClickListener mItemClickListener;
    private RecyclerView.ViewHolder mHeaderViewHolder;

    public interface OnItemClickListener {
        void onItemClicked(View view, int postion);
    }

    public HomeListAdapter(Context context, LayoutInflater layoutInflater) {
        mContext = context;
        mData = new ArrayList<HomeItem>();
        mLayoutInflater = layoutInflater;
        for (int i = 0; i < 20; i++) {
            Drawable d;
            if (i % 3 == 0) {
                d = mContext.getResources().getDrawable(R.mipmap.pic1);
            } else if (i % 3 == 1) {
                d = mContext.getResources().getDrawable(R.mipmap.pic2);
            } else {
                d = mContext.getResources().getDrawable(R.mipmap.pic3);
            }
            mData.add(new HomeItem("this is item : " + i, d));
        }
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mItemClickListener = listener;
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
        if (viewType == ITEM_TYPE_NORMAL) {
            return new MyViewHolder(mItemClickListener,
                    mLayoutInflater.inflate(R.layout.home_item_view, parent, false));
        } else {
            return mHeaderViewHolder;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (position != 0) {
            ((MyViewHolder)holder).bindTo(mData.get(position), position);
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    private static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView mText;
        private ImageView mImage;
        private int mPostion;
        private OnItemClickListener mOnItemClickListener;
        HomeItem mBoundItem;

        public MyViewHolder(OnItemClickListener listener, View itemView) {
            super(itemView);
            mOnItemClickListener = listener;
            mText = (TextView) itemView.findViewById(R.id.item_text);
            mImage = (ImageView) itemView.findViewById(R.id.item_image);
            itemView.setOnClickListener(this);
        }

        public void bindTo(HomeItem item, int postion) {
            mBoundItem = item;
            mPostion = postion;
            mText.setText(item.mText);
            mImage.setImageDrawable(item.mPicture);
        }

        @Override
        public void onClick(View view) {
            mOnItemClickListener.onItemClicked(view, mPostion);
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
