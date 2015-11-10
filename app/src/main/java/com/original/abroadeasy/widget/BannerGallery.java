package com.original.abroadeasy.widget;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.widget.Gallery;

public class BannerGallery extends Gallery {
    
    private static final int SHOW_NEXT = 0;
    private static final long DELAY_TIME = 3000;
    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message arg0) {
            if (arg0.what == SHOW_NEXT) {
                BannerGallery.this.showNext();
                sendEmptyMessageDelayed(SHOW_NEXT, DELAY_TIME);
            }
        }
    };

    public BannerGallery(Context context) {
        super(context);
    }

    public BannerGallery(Context context, AttributeSet attrs) {
        super(context, attrs);
        mHandler.sendEmptyMessageDelayed(SHOW_NEXT, DELAY_TIME);
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        mHandler.removeMessages(SHOW_NEXT);
        mHandler.sendEmptyMessageDelayed(SHOW_NEXT, DELAY_TIME * 2);
        int keycode;
        if (velocityX < 0) {
            keycode = KeyEvent.KEYCODE_DPAD_RIGHT;
        } else {
            keycode = KeyEvent.KEYCODE_DPAD_LEFT;
        }
        onKeyDown(keycode, null);
        return true;
//        return super.onFling(e1, e2, velocityX > 0 ? 3500 : -3500, velocityY);
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return super.onScroll(e1, e2, distanceX, distanceY);
    }

    public void showPrevious() {
        onKeyDown(KeyEvent.KEYCODE_DPAD_LEFT, null);
    }

    public void showNext() {
        onKeyDown(KeyEvent.KEYCODE_DPAD_RIGHT, null);
    }
}
