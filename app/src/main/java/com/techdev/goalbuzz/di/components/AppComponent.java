package com.techdev.goalbuzz.di.components;

import android.content.Context;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.techdev.goalbuzz.di.modules.AppModule;
import com.techdev.goalbuzz.di.modules.NetworkModule;
import com.techdev.goalbuzz.di.scopes.AmazonScope;
import com.techdev.goalbuzz.di.scopes.BatScope;
import com.techdev.goalbuzz.di.scopes.DBSportsScope;
import com.techdev.goalbuzz.di.scopes.FootballScope;
import com.techdev.goalbuzz.di.scopes.ApplicationContext;
import com.techdev.goalbuzz.network.client.RetrofitClient;
import com.techdev.goalbuzz.network.prefs.PrefManager;
import com.techdev.goalbuzz.room.database.RoomManager;
import com.techdev.goalbuzz.util.ResourceUtils;

import javax.inject.Singleton;
import dagger.Component;

@Singleton
@Component(
        modules = {
                AppModule.class,
                NetworkModule.class
        }
)
public interface AppComponent {

    @ApplicationContext
    Context getContext();

    @DBSportsScope
    RetrofitClient getRetrofitDbClient();

    @FootballScope
    RetrofitClient getRetrofitClient();

    @AmazonScope
    RetrofitClient getRetrofit();

    @BatScope
    RetrofitClient getBatRetrofit();

    PrefManager getPrefManger();

    ResourceUtils getResourceUtils();

    RoomManager getAppDatabase();

    FirebaseAnalytics getFirebaseAnalytics();
}
