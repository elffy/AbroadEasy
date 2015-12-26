package com.original.abroadeasy.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

/**
 * Created by zengjinlong on 15-12-15.
 */
public class DetailFragmentAdapter extends BaseAdapter {
    private ArrayList<View> mViews = new ArrayList<View>();
    private Context mContext;

    public DetailFragmentAdapter(Context context) {
        mContext = context;
    }

    public void addView(View view) {
        mViews.add(view);
    }

    @Override
    public int getCount() {
        return mViews.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = mViews.get(i);
        // setOnClickListener to avoid the background state change handled by listView when press.
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //do nothing.
            }
        });
        return mViews.get(i);
    }
}
