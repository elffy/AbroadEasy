package com.original.abroadeasy.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.Toast;

import com.original.abroadeasy.Listeners.OnGridItemSelectedListener;
import com.original.abroadeasy.R;
import com.original.abroadeasy.activity.AnswerListActivity;
import com.original.abroadeasy.adapter.FindGridAdapter;
import com.original.abroadeasy.activity.MainActivity;
import com.original.abroadeasy.widget.CustomSearchBar;
import com.original.abroadeasy.widget.GridMarginDecoration;
import com.original.abroadeasy.widget.TwoRowLayout;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Yangli on 15-12-08.
 */
public class ExploreFragment extends BaseFragment implements OnGridItemSelectedListener{

    private View mView;

    private FindHandler mFindHandler;

    @Bind(R.id.recycler_view_find)
    RecyclerView mRecyclerView;
    @Bind(R.id.search_view_stub)
    ViewStub mSearchViewStub;

    private View mSearchView;

    public ExploreFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_explore, container, false);
        ButterKnife.bind(this, mView);
        final FindGridAdapter findGridAdapter = new FindGridAdapter(mActivity, ExploreFragment.this,
                mActivity.getLayoutInflater(), R.array.icons);

        final GridLayoutManager gridLayoutManager = new GridLayoutManager(mActivity, 2);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                //return gridLayoutManager.getSpanCount();
                return 1;
            }
        });
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mRecyclerView.addItemDecoration(new GridMarginDecoration(mActivity, 2, 2, 2, 2));
        mRecyclerView.setAdapter(findGridAdapter);
        mRecyclerView.addOnScrollListener(mRecylerViewScrollListener);
        mFindHandler = new FindHandler();

        return mView;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onRefresh() {
        mFindHandler.sendMessageDelayed(mFindHandler.obtainMessage(MSG_FIND_REFRESH, 0, 0), 1000);
    }

    @Override
    public boolean handleBackKey() {
        if (mSearchView.getVisibility() == View.VISIBLE) {
            searchCanceled();
            return true;
        }
        return false;
    }

    @Override
    public void onFabClicked() {
        if (mSearchView == null) {
            mSearchView = mSearchViewStub.inflate();
            CustomSearchBar searchBar = (CustomSearchBar) mSearchView.findViewById(R.id.search_bar);
            searchBar.setListener(mSearchListener);
            mSearchView.findViewById(R.id.btn_filter).setSelected(true);
            TwoRowLayout category = (TwoRowLayout) mSearchView.findViewById(R.id.program_category);
            category.setArrayResId(R.array.program_category);
            TwoRowLayout location = (TwoRowLayout) mSearchView.findViewById(R.id.program_location);
            location.setArrayResId(R.array.program_locations);
            TwoRowLayout date = (TwoRowLayout) mSearchView.findViewById(R.id.program_date);
//            date.setArrayResId(R.array.program_);
            TwoRowLayout length = (TwoRowLayout) mSearchView.findViewById(R.id.program_length);
            length.setArrayResId(R.array.program_length);
            TwoRowLayout price = (TwoRowLayout) mSearchView.findViewById(R.id.program_price);
            price.setArrayResId(R.array.program_price);
        }
        mSearchView.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.INVISIBLE);
        ((MainActivity)mActivity).mFloatingActionBtn.setVisibility(View.GONE);
        setSwipeEnable(false);
    }
    CustomSearchBar.SearchBarListener mSearchListener = new CustomSearchBar.SearchBarListener() {
        @Override
        public void onQueryTextChange(String newString) {

        }

        @Override
        public void cancelSearch() {
            searchCanceled();
        }

        @Override
        public void startSearch(String queryText) {

        }
    };

    private void searchCanceled() {
        mSearchView.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
        setSwipeEnable(true);
        ((MainActivity)mActivity).mFloatingActionBtn.setVisibility(View.VISIBLE);
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroyView() {

        super.onDestroyView();
        ButterKnife.unbind(this);
        mFindHandler.removeCallbacksAndMessages(null);
    }

    private static final int MSG_FIND_REFRESH = 0;

    @Override
    public void onGridItemClick(View view, int pos) {
        Toast.makeText(mActivity, "Grid item clicked is :" + pos, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(mActivity, AnswerListActivity.class);
        startActivity(intent);
    }

    private class FindHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            int what = msg.what;
            if (MSG_FIND_REFRESH == what) {
                if (msg.arg1 < 3) {
                    msg.arg1++;
                    sendMessageDelayed(mFindHandler.obtainMessage(MSG_FIND_REFRESH, msg.arg1 + 1, 0), 1000);
                } else {
                    setRefreshing(false);
                }
            }
        }
    }


}
