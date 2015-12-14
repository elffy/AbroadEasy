package com.original.abroadeasy.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
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
import com.original.abroadeasy.app.App;
import com.original.abroadeasy.model.HomeItem;
import com.original.abroadeasy.model.ProgramItem;
import com.original.abroadeasy.network.NetworkUtil;
import com.original.abroadeasy.util.LogUtil;
import com.original.abroadeasy.widget.BannerGallery;

import java.util.ArrayList;
import java.util.List;

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
        initData();
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
        mHandler.sendEmptyMessage(MSG_REFRESH);
    }

    private static final int MSG_REFRESH = 0;
    private static final int MSG_LOAD_DONE = 1;

    private class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_LOAD_DONE:
                    setRefreshing(false);
                    mLoadTask = null;
                    if (mPendLoadType > 0) {
                        mLoadTask = new LoadDataTask(mPendLoadType);
                        mLoadTask.execute(mPendLoadType == LOAD_MORE ? mLoadIndex : 0);
                        mPendLoadType = -1;
                    }
                    break;
                case MSG_REFRESH:
                    loadNew();
                    break;
            }
        }
    }

    private LinearLayoutManager mLinearLayoutManager;
    private HomeListAdapter mAdapter;
    private List<ProgramItem> mDatas = new ArrayList<ProgramItem>();

    private void intiView() {

        // init banner first.
        initBanner();

        // init recyclerView.
        mLinearLayoutManager = new LinearLayoutManager(mActivity);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mAdapter = new HomeListAdapter(mActivity, mActivity.getLayoutInflater(), mDatas);
        mAdapter.setOnItemClickListener(new HomeListAdapter.OnItemClickListener() {
            @Override
            public void onItemClicked(View view, int postion) {
                startDetailActivity();
            }
        });
        mAdapter.setmHeaderViewHolder(mHeaderViewHolder);
        mAdapter.setScrollListener(new HomeListAdapter.OnScrollListener() {
            @Override
            public void onScrollToEnd() {
                loadMore();
            }
        });
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

    private static final int LOAD_MORE = 1;
    private static final int LOAD_NEW= 2;
    private int mPendLoadType = 0;
    private List<ProgramItem> mNewPrograms;
    private int mLoadIndex = 0;
    private LoadDataTask mLoadTask;
    private void initData() {
        if (!NetworkUtil.isNetworkAvailable(mActivity)) {
            // TODO add Toast
            return;// network unavailable, just return;
        }
        mLoadTask = new LoadDataTask(LOAD_MORE);
        mLoadTask.execute(mLoadIndex);
    }

    class LoadDataTask extends AsyncTask<Integer, Void, Void> {

        private int mLoadType;
        LoadDataTask(int type) {
            mLoadType = type;
        }

        @Override
        protected Void doInBackground(Integer[] params) {
            int index = 0;
            if (params != null && params.length > 0) {
                index = params[0];
            }
            try {
                mNewPrograms = App.getRetrofitService().getProgramList(index);
            } catch (Exception e) {
                LogUtil.e("doInBackground, Exception:" + e.toString());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void o) {
            if (mNewPrograms != null && mNewPrograms.size() > 0) {
                if (mLoadType == LOAD_NEW) {
                    int oldFirstId = mDatas.get(0).id;
                    for (ProgramItem item : mNewPrograms) {
                        if (item.id < oldFirstId) {
                            // add new items at head
                            mDatas.add(0, item);
                            mLoadIndex++;
                        }
                    }
                } else {
                    mLoadIndex += mNewPrograms.size();
                    mDatas.addAll(mNewPrograms);
                }
                mNewPrograms.clear();
                mAdapter.notifyDataSetChanged();
            }
            mHandler.sendEmptyMessage(MSG_LOAD_DONE);
        }
    }

    private void loadMore() {
        if (mLoadTask == null) {
            mLoadTask = new LoadDataTask(LOAD_MORE);
            mLoadTask.execute(mLoadIndex);
        } else {
            mPendLoadType = LOAD_MORE;
        }
    }

    private void loadNew() {
        if (mLoadTask == null) {
            mLoadTask = new LoadDataTask(LOAD_NEW);
            mLoadTask.execute(0);
        } else {
            mPendLoadType = LOAD_NEW;
        }
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
