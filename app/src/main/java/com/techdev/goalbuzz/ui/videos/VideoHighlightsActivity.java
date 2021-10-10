package com.techdev.goalbuzz.ui.videos;

import android.os.Bundle;

import com.techdev.goalbuzz.R;
import com.techdev.goalbuzz.core.presentation.base.BaseActivity;

public class VideoHighlightsActivity extends BaseActivity<VideoHighlightsContract.Presenter> {

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_video_highlights;
    }

    @Override
    protected int getMenuRes() {
        return 0;
    }

    @Override
    protected void onConnectivityTask(boolean isConnected) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
}