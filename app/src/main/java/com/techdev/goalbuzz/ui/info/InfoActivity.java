package com.techdev.goalbuzz.ui.info;

import android.os.Bundle;

import androidx.databinding.ViewDataBinding;

import com.google.android.material.snackbar.Snackbar;
import com.techdev.goalbuzz.R;
import com.techdev.goalbuzz.databinding.ActivityInfoBinding;
import com.techdev.goalbuzz.di.components.ActivityComponent;
import com.techdev.goalbuzz.ui.base.BaseActivity;

public class InfoActivity extends BaseActivity<InfoContract.Presenter> implements InfoContract.View {

    private Snackbar snackbar;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_info;
    }

    @Override
    protected int getMenuRes() {
        return 0;
    }

    @Override
    protected void onConnectivityTask(boolean isConnected) {

    }

    @Override
    protected void initViewBinding(ViewDataBinding viewBinding) {
        com.techdev.goalbuzz.databinding.ActivityInfoBinding infoBinding = (ActivityInfoBinding) viewBinding;
    }

    @Override
    protected void injectComponent(ActivityComponent activityComponent) {
        activityComponent.inject(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // initialize snackBar
        this.snackbar = getSnack();
    }

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
    protected void onDestroy() {
        hideLoader();
        snackbar = null;
        super.onDestroy();
    }
}