package com.techdev.goalbuzz.core.adapter.event;

import android.os.SystemClock;
import android.view.View;

public abstract class OnSingleClickListener implements View.OnClickListener {

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
    public abstract void onSingleClick(View v);

    @Override
    public final void onClick(View v) {

        long currentClickTime = SystemClock.uptimeMillis();

        long elapsedTime = currentClickTime - mLastClickTime;

        mLastClickTime = currentClickTime;

        if(elapsedTime <= MIN_CLICK_INTERVAL) {
            return;
        }
        onSingleClick(v);
    }

}

