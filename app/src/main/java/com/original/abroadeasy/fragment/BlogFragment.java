package com.original.abroadeasy.fragment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.original.abroadeasy.R;
import com.original.abroadeasy.adapter.BlogListAdapter;
import com.original.abroadeasy.datas.beans.BeansUtils;
import com.original.abroadeasy.datas.beans.MovieInfoBean;
import com.original.abroadeasy.datas.beans.MovieMajorInfos;
import com.original.abroadeasy.datas.beans.MovieUSBox;
import com.original.abroadeasy.datas.beans.entities.SubjectEntity;
import com.original.abroadeasy.datas.beans.entities.SubjectsEntity;
import com.original.abroadeasy.network.DoubanApiUtils;
import com.original.abroadeasy.network.NetworkUtil;
import com.original.abroadeasy.activity.BlogDetailActivity;
import com.original.abroadeasy.util.LogUtil;

import java.util.ArrayList;
import java.util.List;

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

    private List<MovieInfoBean> mNewPrograms = new ArrayList<MovieInfoBean>();

    private static final int LOAD_MORE = 1;
    private static final int LOAD_NEW= 2;
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

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_blog, container, false);
        ButterKnife.bind(this, mView);
        initView();
        mBlogHandler = new BlogHandler();
        return mView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initData();
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
    private BlogListAdapter mBlogAdapter;
    private void initView() {

        mLinearLayoutManager = new LinearLayoutManager(mActivity);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mBlogAdapter = new BlogListAdapter(this, mActivity.getLayoutInflater(), mNewPrograms);
        mRecyclerView.setAdapter(mBlogAdapter);
        mBlogAdapter.setOnItemClickListener(new BlogListAdapter.OnItemClickListener() {
            @Override
            public void onItemClicked(View view, int postion) {
                MovieMajorInfos movieMajorInfos = new MovieMajorInfos();
                MovieInfoBean mBean = mNewPrograms.get(postion);
                movieMajorInfos.fillDatas(mBean.getId(), mBean.getTitle(), mBean.getImageUri(),
                        mBean.getCastsCount(), mBean.getCastsIds(), mBean.getCastsAvatorUris(),
                        mBean.getDirectorId(), mBean.getDirectorImageUri(), mBean.getAverage(), mBean.getFormatedGenres());
                Intent intent = new Intent(mActivity, BlogDetailActivity.class);
                intent.putExtra(BeansUtils.MOVIE_MAJOR_INFOS_KEY, movieMajorInfos);
                startActivity(intent);
            }
        });

        //if do nothing recyllerview wil refresh everywhere
        mRecyclerView.addOnScrollListener(mRecylerViewScrollListener);
    }

    /*private class BlogAdapter extends RecyclerView.Adapter<MyViewHolder> {
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
    }*/

    public static boolean mFirstLoad = true;
    class LoadDataTask extends AsyncTask<Integer, Void, Void> {

        private int mLoadType;
        LoadDataTask(int type) {
            mLoadType = type;
        }

        @Override
        protected void onPreExecute() {
            //if (mLoadType == LOAD_MORE) {
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
            //}
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
            mBlogAdapter.notifyDataSetChanged();
            //mHandler.sendEmptyMessage(MSG_LOAD_DONE);
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

                mNewPrograms.add(new MovieInfoBean(subject));
            }
        }


    }
}
