package com.original.abroadeasy.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.original.abroadeasy.R;

import butterknife.Bind;
import butterknife.ButterKnife;

public class LaunchAnimActivity extends BaseActivity

{
    @Bind(R.id.ic_launch_title)
    ImageView imageView;

    @Bind(R.id.tx_launch_title)
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch_anim);
        ButterKnife.bind(this);

        Animation operatingAnim = AnimationUtils.loadAnimation(this, R.anim.luancher_img_repeat);
        imageView.setAnimation(operatingAnim);
        operatingAnim.start();

        TranslateAnimation alphaAnimation2 = new TranslateAnimation(0, 0, 0,
                -70);
        alphaAnimation2.setDuration(800);
        alphaAnimation2.setRepeatCount(3);
        alphaAnimation2.setRepeatMode(Animation.REVERSE);
        textView.setAnimation(alphaAnimation2);
        alphaAnimation2.start();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                Intent intent = new Intent(LaunchAnimActivity.this , MainActivity.class);
                startActivity(intent);
                LaunchAnimActivity.this.finish();
            }
        } , 6000);
    }

    @Override
    protected void onDestroy() {
        ButterKnife.unbind(this);
        super.onDestroy();
    }

}
