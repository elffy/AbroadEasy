package com.original.abroadeasy.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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

import com.original.abroadeasy.R;
import com.original.abroadeasy.adapter.HomeListAdapter;
import com.original.abroadeasy.datas.beans.BeansUtils;
import com.original.abroadeasy.datas.beans.MovieInfoBean;
import com.original.abroadeasy.datas.beans.MovieMajorInfos;
import com.original.abroadeasy.datas.beans.MovieUSBox;
import com.original.abroadeasy.datas.beans.entities.SubjectEntity;
import com.original.abroadeasy.datas.beans.entities.SubjectsEntity;
import com.original.abroadeasy.model.HomeItem;
import com.original.abroadeasy.network.DoubanApiUtils;
import com.original.abroadeasy.network.NetworkUtil;
import com.original.abroadeasy.activity.DetailActivity;
import com.original.abroadeasy.util.LiteOrmDBUtil;
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
        mHandler = new MyHandler();
        return mView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initData();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
        mDatas.clear();
        mNewPrograms.clear();
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
                    long delay = 200;
                    if (mFirstLoad) {
                        mFirstLoad = false;
                        delay = 300;
                    }
                    postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            setRefreshing(false);
                        }
                    }, delay);
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
    private List<MovieInfoBean> mDatas = new ArrayList<MovieInfoBean>();

    private void intiView() {

        // init banner first.
        initBanner();

        // init recyclerView.
        mLinearLayoutManager = new LinearLayoutManager(mActivity);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mAdapter = new HomeListAdapter(this, mActivity.getLayoutInflater(), mNewPrograms);
        mAdapter.setOnItemClickListener(new HomeListAdapter.OnItemClickListener() {
            @Override
            public void onItemClicked(View view, int postion) {
                MovieMajorInfos movieMajorInfos = new MovieMajorInfos();
                MovieInfoBean mBean = mDatas.get(postion - 1);
                movieMajorInfos.fillDatas(mBean.getId(), mBean.getTitle(), mBean.getImageUri(),
                        mBean.getCastsCount(), mBean.getCastsIds(), mBean.getCastsAvatorUris(),
                        mBean.getDirectorId(), mBean.getDirectorImageUri(), mBean.getAverage(), mBean.getFormatedGenres());
                startDetailActivity(movieMajorInfos);
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
        mRecyclerView.addOnScrollListener(mRecylerViewScrollListener);
    }

    private static final int LOAD_MORE = 1;
    private static final int LOAD_NEW= 2;
    private int mPendLoadType = 0;
    //private List<ProgramItem> mNewPrograms;
    private List<HomeItem> mNewPrograms = new ArrayList<HomeItem>();
    private int mLoadIndex = 0;
    private LoadDataTask mLoadTask;
    private void initData() {
        if (!NetworkUtil.isNetworkAvailable(mActivity)) {
            // TODO add Toast
            return;// network unavailable, just return;
        }
        //Don't do this in the beginning
        /*mLoadTask = new LoadDataTask(LOAD_MORE);
        mLoadTask.execute(mLoadIndex);*/

        loadCacheData();
    }

    public static boolean mFirstLoad = true;
    class LoadDataTask extends AsyncTask<Integer, Void, Void> {

        private int mLoadType;
        LoadDataTask(int type) {
            mLoadType = type;
        }

        @Override
        protected void onPreExecute() {
            if (mLoadType == LOAD_MORE) {
                if (mFirstLoad) {
                    // trigger SwipeRefreshLayout to show or wait onMeasure called.
//                    mSwipeRefreshLayout.setProgressViewOffset(false, -26 * 3, 64 * 3);
                    mSwipeRefreshLayout.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            setRefreshing(true);
                        }
                    }, 100);
                } else {
                    setRefreshing(true);
                }
            }
        }

        @Override
        protected Void doInBackground(Integer[] params) {
            int index = 0;
            if (params != null && params.length > 0) {
                index = params[0];
            }
            try {
                //mNewPrograms = App.getRetrofitService().getProgramList(index);
                MovieUSBox object = DoubanApiUtils.getMovieApiService().getMoviceUSBox(DoubanApiUtils.API_KEY);
                collectResultsFromResponse(object);
            } catch (Exception e) {
                LogUtil.e("doInBackground, Exception:" + e.toString());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void o) {
            /*if (mNewPrograms != null && mNewPrograms.size() > 0) {
                if (mLoadType == LOAD_NEW && mDatas.size() > 0) {
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
            mHandler.sendEmptyMessage(MSG_LOAD_DONE);*/

            //mNewPrograms.clear();
            mAdapter.notifyDataSetChanged();
            mHandler.sendEmptyMessage(MSG_LOAD_DONE);
        }
    }

    public void collectResultsFromResponse(Object object) {
        if (object == null) {
            return;
        }

        List<SubjectsEntity> subjects;
        SubjectsEntity subjectsEntity;
        SubjectEntity subject;

        int i;

        MovieUSBox usBox = (MovieUSBox)object;
        subjects = usBox.getSubjects();
        if (subjects != null) {
            for (i = 0; i < subjects.size(); i++) {
                subjectsEntity = subjects.get(i);
                if (subjectsEntity == null) {
                    continue;
                }
                subject = subjectsEntity.getSubject();
                if (subject == null) {
                    continue;
                }

                LogUtil.d(subject.getTitle());

                //Fill the cache
                MovieInfoBean movieInfo = new MovieInfoBean(subject);
                HomeItem homeItem = new HomeItem();
                homeItem.fillDatas(movieInfo.getTitle(), movieInfo.getImageUri(),
                        movieInfo.getAverage(), movieInfo.getFormatedGenres());
                //插入数据库 这种耗时的操作放在doInBackgroud比较合适
                if (!isExistTitle(homeItem.getTitle())) {
                    //加入到列表中
                    mNewPrograms.add(homeItem);
                    //添加到数据库
                    LiteOrmDBUtil.insert(homeItem);

                }
                //添加前需要clear以下吗
                mDatas.add(movieInfo);

            }
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

    private void startDetailActivity(MovieMajorInfos movieInfos) {
        Intent intent = new Intent(mActivity, DetailActivity.class);
        intent.putExtra(BeansUtils.MOVIE_MAJOR_INFOS_KEY, movieInfos);
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
                //这里用于链接到网页
                startGitHubWeb();
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

    private void startGitHubWeb() {
        Uri uri = Uri.parse("https://github.com/dxjia/DoubanTop");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    private void loadCacheData() {
        List<HomeItem> temItems = LiteOrmDBUtil.getQueryAll(HomeItem.class);
        if (0 < temItems.size()) {
            mNewPrograms.addAll(temItems);
            mAdapter.notifyDataSetChanged();
        }
    }

    private boolean isExistTitle(String title) {
        String field = "title";
        String[] value = new String[]{title};
        List<HomeItem> tempList = LiteOrmDBUtil.getQueryByWhere(HomeItem.class, field, value);
        if (null != tempList && 0 < tempList.size()) {
            return true;
        }

        return  false;
    }
}
