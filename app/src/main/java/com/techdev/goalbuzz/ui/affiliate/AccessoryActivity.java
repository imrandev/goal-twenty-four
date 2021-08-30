package com.techdev.goalbuzz.ui.affiliate;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.material.snackbar.Snackbar;
import com.techdev.goalbuzz.R;
import com.techdev.goalbuzz.adapter.BaseRecyclerClickListener;
import com.techdev.goalbuzz.adapter.BaseRecyclerViewHolder;
import com.techdev.goalbuzz.adapter.RecyclerViewAdapter;
import com.techdev.goalbuzz.databinding.ActivityAccessoryBinding;
import com.techdev.goalbuzz.di.components.ActivityComponent;
import com.techdev.goalbuzz.model.market.Product;
import com.techdev.goalbuzz.ui.base.BaseActivity;
import com.techdev.goalbuzz.ui.fixture.FixtureActivity;
import com.techdev.goalbuzz.util.CustomTabActivityHelper;
import com.techdev.goalbuzz.util.WebViewFallback;
import com.techdev.goalbuzz.viewholder.AccessoryViewHolder;

import java.util.List;
import java.util.Objects;

public class AccessoryActivity extends BaseActivity<AccessoryContract.Presenter> implements AccessoryContract.View {

    private static final String TAG = "AccessoryActivity";

    private ActivityAccessoryBinding accessoryBinding;
    private Snackbar snackbar;
    private AccessoryContract.Presenter mAccessoryContract;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_accessory;
    }

    @Override
    protected int getMenuRes() {
        return 0;
    }

    @Override
    protected void onConnectivityTask(boolean isConnected) {
        accessoryBinding.tvOfflineStatus.setVisibility(isConnected ? View.GONE : View.VISIBLE);
        mAccessoryContract.executeAccessoryTask();
    }

    @Override
    protected void initViewBinding(ViewDataBinding viewBinding) {
        accessoryBinding = (ActivityAccessoryBinding) viewBinding;
    }

    @Override
    protected void injectComponent(ActivityComponent activityComponent) {
        activityComponent.inject(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Sports Accessories");
        toolbar.setSubtitle("From Amazon");
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        snackbar = getSnack();

        accessoryBinding.rvAccessoryList.setLayoutManager(new LinearLayoutManager(this));
        accessoryBinding.rvAccessoryList.setHasFixedSize(true);
        mAccessoryContract = presenter;
        mAccessoryContract.onAttach(this);
    }

    @Override
    public void showMessage(String message) {
        toast(message);
    }

    @Override
    public void showLoader() {
        snackbar.show();
    }

    @Override
    public void hideLoader() {
        snackbar.dismiss();
    }

    @Override
    public void onResponse(List<Product> products, String message) {
        RecyclerViewAdapter<Product, BaseRecyclerClickListener<Product>> accessoryAdapter = new RecyclerViewAdapter<Product,
                BaseRecyclerClickListener<Product>>(products, buyNowClickListener,  message) {
            @Override
            public BaseRecyclerViewHolder<Product, BaseRecyclerClickListener<Product>> getViewHolder(ViewGroup parent) {
                ViewDataBinding dataBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                        R.layout.item_accessory, parent, false);
                return new AccessoryViewHolder(dataBinding);
            }

            @Override
            public BaseRecyclerViewHolder<Product, BaseRecyclerClickListener<Product>> getSingleHeaderViewHolder(ViewGroup parent) {
                return null;
            }

            @Override
            public BaseRecyclerViewHolder<Product, BaseRecyclerClickListener<Product>> getEmptyViewHolder(ViewGroup parent) {
                ViewDataBinding dataBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                        R.layout.item_empty_view, parent, false);
                return new AccessoryViewHolder(dataBinding);
            }
        };
        accessoryBinding.rvAccessoryList.setAdapter(accessoryAdapter);

        // show ad
        onViewAd();
    }

    private final BaseRecyclerClickListener<Product> buyNowClickListener = new BaseRecyclerClickListener<Product>() {
        @Override
        public void onItemClickListener(View view, Product product, int position) {
            CustomTabsIntent customTabsIntent = new CustomTabsIntent.Builder().build();
            CustomTabActivityHelper.openCustomTab(
                    AccessoryActivity.this, customTabsIntent, Uri.parse(product.getLinkUrl()), new WebViewFallback());
        }
    };

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
        accessoryBinding = null;
    }

    private void onViewAd(){
        InterstitialAd.load(getApplicationContext(),
                getString(R.string.interstitial_ads), new AdRequest.Builder().build(), adLoadCallback);
    }

    private final InterstitialAdLoadCallback adLoadCallback = new InterstitialAdLoadCallback() {
        @Override
        public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
            interstitialAd.show(AccessoryActivity.this);
        }

        @Override
        public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
            Log.i(TAG, loadAdError.getMessage());
        }
    };
}