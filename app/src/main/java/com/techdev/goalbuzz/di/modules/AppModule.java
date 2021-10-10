package com.techdev.goalbuzz.di.modules;

import android.content.Context;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.techdev.goalbuzz.app.GoalKick;
import com.techdev.goalbuzz.di.scopes.ApplicationContext;
import com.techdev.goalbuzz.featureMain.domain.models.Match;
import com.techdev.goalbuzz.core.datasource.local.prefs.PrefManager;
import com.techdev.goalbuzz.core.datasource.local.db.database.DatabaseManager;
import com.techdev.goalbuzz.service.marquee.IMarqueeService;
import com.techdev.goalbuzz.service.marquee.MatchMarqueeService;
import com.techdev.goalbuzz.core.util.ResourceUtils;

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
    static DatabaseManager provideRoomDatabase(@ApplicationContext Context context) {
        return DatabaseManager.getInstance(context);
    }

    @Provides
    FirebaseAnalytics provideFirebaseAnalytics(){
        return FirebaseAnalytics.getInstance(provideContext());
    }

    @Provides
    IMarqueeService<Match> provideMatchMarquee(){
        return MatchMarqueeService.getInstance();
    }
}
