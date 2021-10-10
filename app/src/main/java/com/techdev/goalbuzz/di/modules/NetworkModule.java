package com.techdev.goalbuzz.di.modules;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.techdev.goalbuzz.core.datasource.remote.client.ApiClient;
import com.techdev.goalbuzz.di.scopes.AmazonScope;
import com.techdev.goalbuzz.di.scopes.BatScope;
import com.techdev.goalbuzz.di.scopes.DBSportsScope;
import com.techdev.goalbuzz.di.scopes.FootballScope;
import com.techdev.goalbuzz.di.scopes.ApplicationContext;
import com.techdev.goalbuzz.core.datasource.remote.client.ApiRepository;
import com.techdev.goalbuzz.di.scopes.WithToken;
import com.techdev.goalbuzz.core.util.Constant;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.techdev.goalbuzz.core.util.Constant.MAX_CACHE_SIZE;

@Module
public class NetworkModule {

    private static final String TAG = "NetworkModule";

    private final Context context;

    public NetworkModule(@ApplicationContext Context context) {
        this.context = context;
    }

    @Provides
    @Singleton
    Gson provideGson() {
        return new GsonBuilder().setLenient().create();
    }

    @Provides
    @Singleton
    @WithToken
    OkHttpClient provideOkHttpClient(Cache cache) {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(provideOfflineCacheInterceptor(true));
        httpClient.cache(cache);
        return httpClient.build();
    }

    @Provides
    @Singleton
    OkHttpClient provideOkHttpClientWithoutToken(Cache cache) {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(provideOfflineCacheInterceptor(false));
        httpClient.cache(cache);
        return httpClient.build();
    }

    @Provides
    @Singleton
    @FootballScope
    Retrofit provideApiRetrofit(Gson gson, @WithToken OkHttpClient okHttpClient) {
        return new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(Constant.BASE_URL)
                .client(okHttpClient)
                .build();
    }

    @Provides
    @Singleton
    @DBSportsScope
    Retrofit provideDbRetrofit(Gson gson, OkHttpClient okHttpClient) {
        return new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(Constant.BASE_DB_URL)
                .client(okHttpClient)
                .build();
    }

    @Provides
    @Singleton
    @BatScope
    Retrofit provideBatRetrofit(Gson gson, OkHttpClient okHttpClient) {
        return new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(Constant.BASE_BAT_URL)
                .client(okHttpClient)
                .build();
    }

    @Provides
    @Singleton
    @AmazonScope
    Retrofit provideRetrofit(Gson gson, OkHttpClient okHttpClient) {
        return new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl("https://goal24dev.web.app/")
                .client(okHttpClient)
                .build();
    }

    @Provides
    @Singleton
    @FootballScope
    static ApiClient provideApiClient(@FootballScope Retrofit retrofit){
        ApiRepository apiRepository = retrofit.create(ApiRepository.class);
        return new ApiClient(apiRepository);
    }

    @Provides
    @Singleton
    @DBSportsScope
    static ApiClient provideDbApiClient(@DBSportsScope Retrofit retrofit){
        ApiRepository apiRepository = retrofit.create(ApiRepository.class);
        return new ApiClient(apiRepository);
    }

    @Provides
    @Singleton
    @AmazonScope
    static ApiClient provideAmazonApiClient(@AmazonScope Retrofit retrofit){
        ApiRepository apiRepository = retrofit.create(ApiRepository.class);
        return new ApiClient(apiRepository);
    }

    @Provides
    @Singleton
    @BatScope
    static ApiClient provideBatApiClient(@BatScope Retrofit retrofit){
        ApiRepository apiRepository = retrofit.create(ApiRepository.class);
        return new ApiClient(apiRepository);
    }

    @Provides
    @Singleton
    Cache provideCache(@ApplicationContext Context context) {
        Cache cache = null;
        try {
            cache = new Cache(new File(context.getCacheDir(), "http-cache"), MAX_CACHE_SIZE);
        } catch (Exception e) {
            Log.e(TAG, "Could not create Cache!");
        }
        return cache;
    }

    private boolean hasNetwork(){
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = null;
        if (cm != null) {
            activeNetwork = cm.getActiveNetworkInfo();
        }
        return activeNetwork != null && activeNetwork.isConnected();
    }

    private Interceptor provideOfflineCacheInterceptor(boolean hasToken){
        return chain -> {
            try {
                CacheControl cacheControl = new CacheControl.Builder()
                        .maxStale(5, TimeUnit.MINUTES)
                        .build();
                Request original = chain.request();
                Request.Builder requestBuilder = original.newBuilder()
                        .cacheControl(hasNetwork() ? cacheControl : CacheControl.FORCE_CACHE)
                        .method(original.method(), original.body());
                if (hasToken)
                    requestBuilder.header("X-Auth-Token", Constant.API_TOKEN);
                Request request = requestBuilder.build();
                return chain.proceed(request);
            } catch (Exception e) {
                throw new IOException(e.getMessage());
            }
        };
    }
}
