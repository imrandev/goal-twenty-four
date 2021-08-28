package com.techdev.goalbuzz.ui.explore;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.ViewDataBinding;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.material.snackbar.Snackbar;
import com.techdev.goalbuzz.R;
import com.techdev.goalbuzz.databinding.ActivityExploreBinding;
import com.techdev.goalbuzz.di.components.ActivityComponent;
import com.techdev.goalbuzz.ui.base.BaseActivity;
import com.techdev.goalbuzz.util.Constant;

import java.util.Objects;

public class ExploreActivity extends BaseActivity<ExploreContract.Presenter> implements ExploreContract.View {

    private static final String TAG = "ExploreActivity";
    private ActivityExploreBinding exploreBinding;
    private Snackbar snackbar;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_explore;
    }

    @Override
    protected int getMenuRes() {
        return 0;
    }

    @Override
    protected void onConnectivityTask(boolean isConnected) {
        exploreBinding.tvOfflineStatus.setVisibility(isConnected ? View.GONE : View.VISIBLE);
        loadWebView(isConnected);
    }

    @Override
    protected void initViewBinding(ViewDataBinding viewBinding) {
        this.exploreBinding = (ActivityExploreBinding) viewBinding;
    }

    @Override
    protected void injectComponent(ActivityComponent activityComponent) {
        activityComponent.inject(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        snackbar = getSnack();

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("More Explore");
        toolbar.setSubtitle("Powered by ScoreBat");
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        exploreBinding.webView.getSettings().setLoadsImagesAutomatically(true);
        exploreBinding.webView.getSettings().setJavaScriptEnabled(true);
        exploreBinding.webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        exploreBinding.webView.setWebViewClient(exploreWebViewClient);
    }

    private void loadWebView(boolean visibility){
        exploreBinding.webView.setVisibility(visibility ? View.VISIBLE : View.GONE);
        // Load the initial URL
        if (visibility){
            exploreBinding.webView.loadData(Constant.SCORE_BAT_HTML, "text/html", "UTF-8");
        }
    }

    private final WebViewClient exploreWebViewClient = new WebViewClient(){
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            showLoader();
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            hideLoader();
            if (!view.canGoBack()){
                onViewAd();
            }
        }
    };

    @Override
    public void showMessage(String message) {
        toast(message);
    }

    @Override
    public void showLoader() {
        show(snackbar);
    }

    @Override
    public void hideLoader() {
        dismiss(snackbar);
    }

    @Override
    public void onBackPressed() {
        if (exploreBinding.webView.canGoBack()){
            exploreBinding.webView.goBack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        snackbar.dismiss();
        snackbar = null;
        exploreBinding = null;
    }

    private void onViewAd(){
        InterstitialAd.load(getApplicationContext(),
                getString(R.string.interstitial_ads), new AdRequest.Builder().build(), adLoadCallback);
    }

    private final InterstitialAdLoadCallback adLoadCallback = new InterstitialAdLoadCallback() {
        @Override
        public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
            interstitialAd.show(ExploreActivity.this);
        }

        @Override
        public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
            Log.i(TAG, loadAdError.getMessage());
        }
    };
}