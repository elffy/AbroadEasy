package com.original.abroadeasy.ui;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.original.abroadeasy.Listeners.OnGridItemSelectedListener;
import com.original.abroadeasy.R;
import com.original.abroadeasy.adapter.FindGridAdapter;
import com.original.abroadeasy.widget.GridMarginDecoration;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Yangli on 15-12-08.
 */
public class FindFragment extends BaseFragment implements OnGridItemSelectedListener{

    private View mView;

    private FindHandler mFindHandler;

    @Bind(R.id.recycler_view_find)
    RecyclerView mRecyclerView;

    public FindFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_find, container, false);
        ButterKnife.bind(this, mView);
        final FindGridAdapter findGridAdapter = new FindGridAdapter(mActivity, FindFragment.this,
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
