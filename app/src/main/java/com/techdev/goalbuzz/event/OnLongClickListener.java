package com.techdev.goalbuzz.event;

import android.os.SystemClock;
import android.view.View;

/**
 * Created by Imran Khan on 3/12/2021.
 * Email : context.imran@gmail.com
 */
public abstract class OnLongClickListener implements View.OnLongClickListener {
    /**
     * click interval
     */
    private static final long MIN_CLICK_INTERVAL = 600;

    /**
     * last click time
     */
    private long mLastClickTime;

    /**
     * click listener
     * @param v The view that was clicked.
     */
    public abstract void onLongPressed(View v);

    @Override
    public boolean onLongClick(View v) {
        long currentClickTime = SystemClock.uptimeMillis();

        long elapsedTime = currentClickTime - mLastClickTime;

        mLastClickTime = currentClickTime;

        if(elapsedTime <= MIN_CLICK_INTERVAL) {
            return false;
        }
        onLongPressed(v);
        return true;
    }
}
