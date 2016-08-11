package com.original.abroadeasy.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.original.abroadeasy.R;
import com.original.abroadeasy.datas.beans.BeansUtils;
import com.original.abroadeasy.datas.beans.MovieMajorInfos;
import com.original.abroadeasy.util.LogUtil;

import butterknife.Bind;
import butterknife.ButterKnife;

public class BlogDetailActivity extends AppCompatActivity {

    private MovieMajorInfos mMovieInfos;

    @Bind(R.id.content_blog)
    TextView mContentTx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog_detail);

        Intent intent = getIntent();
        if (intent == null && savedInstanceState == null) {
            return;
        }
        if (intent != null) {
            mMovieInfos = intent.getParcelableExtra(BeansUtils.MOVIE_MAJOR_INFOS_KEY);
        } else {
            mMovieInfos = savedInstanceState.getParcelable(BeansUtils.MOVIE_MAJOR_INFOS_KEY);
        }
        if (mMovieInfos == null) {
            return;
        }
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }



        CollapsingToolbarLayout toolBarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        toolBarLayout.setTitle(mMovieInfos.getMovieTitle());
        LogUtil.d("mMovieInfos.getDescription() = " + mMovieInfos.getDescription());
        mContentTx.setText(mMovieInfos.getDescription());


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            startActivity(new Intent(this, MainActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
