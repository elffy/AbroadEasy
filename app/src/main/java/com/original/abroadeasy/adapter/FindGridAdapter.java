package com.original.abroadeasy.adapter;

import android.app.Fragment;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.original.abroadeasy.Listeners.OnGridItemSelectedListener;
import com.original.abroadeasy.R;

/**
 * Created by Yang on 15/12/8.
 */
public class FindGridAdapter extends RecyclerView.Adapter<ViewHolder>{

    private Context mContext;
    final LayoutInflater mLayoutInflater;
    private OnItemClickListener mItemClickListener;
    private ViewHolder mHeaderViewHolder;

    private final int mImageResId;

    private OnGridItemSelectedListener mOnGridItemSelectedListener;

    public interface OnItemClickListener {
        void onItemClicked(View view, int postion);
    }

    public FindGridAdapter(Context context, Fragment fragment, LayoutInflater layoutInflater, int imageResId) {

        mContext = context;
        mLayoutInflater = layoutInflater;
        mImageResId = imageResId;
        mOnGridItemSelectedListener = (OnGridItemSelectedListener)fragment;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.find_item_view, parent, false);

        return new FindViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        FindViewHolder mFindViewHolder = (FindViewHolder)holder;
        //Drawable icon = mContext.getResources().obtainTypedArray(mImageResId).getDrawable(position);

        //mFindViewHolder.mImage.setImageDrawable(icon);
        mFindViewHolder.mImage.setImageResource(R.mipmap.im_find_title);

    }

    @Override
    public int getItemCount() {
        return mContext.getResources().getIntArray(mImageResId).length;
    }

    public class FindViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private ImageView mImage;
        private int mPostion;

        public FindViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            mImage = (ImageView) itemView.findViewById(R.id.findIV);
        }

        @Override
        public void onClick(View v) {
            mOnGridItemSelectedListener.onGridItemClick(v, getAdapterPosition());
        }
    }
}
