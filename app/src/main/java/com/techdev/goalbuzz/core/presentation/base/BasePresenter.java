package com.techdev.goalbuzz.core.presentation.base;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.techdev.goalbuzz.di.scopes.AmazonScope;
import com.techdev.goalbuzz.di.scopes.BatScope;
import com.techdev.goalbuzz.di.scopes.DBSportsScope;
import com.techdev.goalbuzz.di.scopes.FootballScope;
import com.techdev.goalbuzz.di.scopes.ApplicationContext;
import com.techdev.goalbuzz.core.datasource.remote.client.ApiClient;
import com.techdev.goalbuzz.core.util.AppExecutors;
import com.techdev.goalbuzz.core.datasource.local.db.database.DatabaseManager;
import com.techdev.goalbuzz.core.datasource.local.db.entities.Admob;
import com.techdev.goalbuzz.core.util.Constant;
import com.techdev.goalbuzz.core.util.ResourceUtils;

import java.util.concurrent.atomic.AtomicInteger;

import javax.inject.Inject;

public class BasePresenter<V extends IBaseView> implements IBasePresenter<V> {

    private static final String TAG = "BasePresenter";
    protected V view;

    @Inject
    @FootballScope
    protected ApiClient apiClient;

    @Inject
    @DBSportsScope
    protected ApiClient retrofitDbClient;

    @Inject
    @AmazonScope
    protected ApiClient amazonClient;

    @Inject
    @BatScope
    protected ApiClient sportsBatClient;

    @Inject
    protected @ApplicationContext Context context;

    @Inject
    protected ResourceUtils resourceUtils;

    @Inject
    protected DatabaseManager databaseManager;

    @Inject
    protected FirebaseAnalytics firebaseAnalytics;

    @Override
    public void onAttach(V view) {
        this.view = view;
    }

    @Override
    public void onDetach() {
        if (view != null){
            view = null;
        }
    }

    @Override
    public boolean isConnected() {
        return isNetworkAvailable(context);
    }

    private boolean isNetworkAvailable(@ApplicationContext Context context){
        ConnectivityManager cm = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = null;
        if (cm != null) {
            activeNetwork = cm.getActiveNetworkInfo();
        }
        return activeNetwork != null && activeNetwork.isConnected();
    }

    @Override
    public void adViewUpdate(int count, String activityName) {
        AppExecutors.getInstance().diskIO().execute(() -> {
            Admob admob = databaseManager.admobDao().findById(activityName);
            if (admob == null){
                admob =  new Admob();
                admob.setAdType("Interstitial");
                admob.setCount(count);
                admob.setName(activityName);
                databaseManager.admobDao().insert(admob);
            } else {
                admob.setCount(count > Constant.AD_INTERVAL ? 0 : count);
                databaseManager.admobDao().update(admob);
            }
        });
    }

    @Override
    public int getAdCount(String activityName) {
        AtomicInteger count = new AtomicInteger();
        AppExecutors.getInstance().diskIO().execute(() -> {
            Admob admob = databaseManager.admobDao().findById(activityName);
            if (admob != null){
                count.set(admob.getCount());
            }
        });
        return count.get();
    }

    @Override
    public boolean showAd(String name){
        Log.d(TAG, "showAd: " + name);
        return getAdCount(name) % Constant.AD_INTERVAL == 0;
    }
}
