package com.techdev.goalbuzz.di.modules;

import android.content.Context;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.techdev.goalbuzz.app.GoalKick;
import com.techdev.goalbuzz.di.scopes.ApplicationContext;
import com.techdev.goalbuzz.network.prefs.PrefManager;
import com.techdev.goalbuzz.room.database.RoomManager;
import com.techdev.goalbuzz.util.ResourceUtils;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {

    private final GoalKick mApplication;

    public AppModule(GoalKick app) {
        mApplication = app;
    }

    @Provides
    @Singleton
    @ApplicationContext
    Context provideContext() {
        return mApplication;
    }

    @Provides
    @Singleton
    static PrefManager providePrefManager(@ApplicationContext Context context) {
        return PrefManager.getInstance(context);
    }

    @Provides
    @Singleton
    static ResourceUtils provideResourceUtils(){
        return ResourceUtils.getInstance();
    }

    @Provides
    @Singleton
    static RoomManager provideRoomDatabase(@ApplicationContext Context context) {
        return RoomManager.getInstance(context);
    }

    @Provides
    FirebaseAnalytics provideFirebaseAnalytics(){
        return FirebaseAnalytics.getInstance(provideContext());
    }
}
