package com.techdev.goalbuzz.app;

import android.app.Application;

import androidx.appcompat.app.AppCompatDelegate;

import com.google.firebase.FirebaseApp;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.techdev.goalbuzz.di.components.AppComponent;
import com.techdev.goalbuzz.di.components.DaggerAppComponent;
import com.techdev.goalbuzz.di.modules.AppModule;
import com.techdev.goalbuzz.di.modules.NetworkModule;

public class GoalKick extends Application {

    private AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        FirebaseApp.initializeApp(this);
        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(true);

        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .networkModule(new NetworkModule(this))
                .build();
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }
}
