package com.techdev.goalbuzz.ui.base;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.MenuRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.transition.Fade;
import androidx.transition.Transition;
import androidx.transition.TransitionManager;

import com.google.android.material.snackbar.Snackbar;
import com.techdev.goalbuzz.R;
import com.techdev.goalbuzz.app.GoalKick;
import com.techdev.goalbuzz.di.components.ActivityComponent;
import com.techdev.goalbuzz.di.components.DaggerActivityComponent;
import com.techdev.goalbuzz.util.ResourceUtils;

import javax.inject.Inject;

public abstract class BaseActivity<P extends IBasePresenter> extends AppCompatActivity {

    @Inject
    public P presenter;

    protected ActivityComponent activityComponent;

    @Inject
    public ResourceUtils resourceUtils;

    @LayoutRes protected abstract int getLayoutRes();
    @MenuRes protected abstract int getMenuRes();
    protected abstract void onConnectivityTask(boolean isConnected);
    protected void injectComponent(ActivityComponent activityComponent){}
    protected void initViewBinding(ViewDataBinding viewBinding) {}

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViewBinding(DataBindingUtil.setContentView(this, getLayoutRes()));
        activityComponent = DaggerActivityComponent.builder()
                .appComponent(((GoalKick) getApplication()).getAppComponent()).build();
        injectComponent(activityComponent);
        registerReceiver(broadcastReceiver,
                new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(broadcastReceiver);
        if (presenter != null) presenter.onDetach();
        super.onDestroy();
    }

    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null){
                String action = intent.getAction() != null ? intent.getAction() : "";
                if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)){
                    onConnectivityTask(presenter.isConnected());
                }
            }
        }
    };

    protected Snackbar getSnack(){
        View rootView = findViewById(R.id.rootView);
        Snackbar snackbar = Snackbar.make(
                rootView, R.string.data_loading, Snackbar.LENGTH_INDEFINITE);
        ViewGroup contentLay = (ViewGroup) snackbar.getView()
                .findViewById(com.google.android.material.R.id.snackbar_text).getParent();
        ProgressBar item = new ProgressBar(this);
        contentLay.addView(item);
        return snackbar;
    }

    protected void toast(String message){
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    protected void show(Snackbar snackbar){
        if (!snackbar.isShown()){
            snackbar.show();
        }
    }

    protected void dismiss(Snackbar snackbar){
        if (snackbar.isShown()){
            snackbar.dismiss();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (getMenuRes() != 0){
            getMenuInflater().inflate(getMenuRes(), menu);
        }
        return super.onCreateOptionsMenu(menu);
    }

    protected void animate(View view, @IdRes int idRes) {
        View rootView = findViewById(R.id.rootView);
        Transition transition = new Fade();
        transition.setDuration(600);
        transition.addTarget(idRes);
        TransitionManager.beginDelayedTransition((ViewGroup) rootView, transition);
        view.setVisibility(view.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
    }
}
