package com.techdev.goalbuzz.core.datasource.remote.response;

import android.util.Log;

import androidx.annotation.NonNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public abstract class EnqueueResponse<T> implements Callback<T> {

    private static final String TAG = "EnqueueResponse";

    @Override
    public void onResponse(@NonNull Call<T> call, @NonNull Response<T> response) {
        int code = response.code();
        String message = response.raw().message();
        if (response.isSuccessful()){
            T result = response.body();
            if (result != null){
                onReceived(result, message);
            }
        } else {
            onError(message, code);
        }
        Log.e(TAG, "onResponse: " + message);
    }

    @Override
    public void onFailure(@NonNull Call<T> call, @NonNull Throwable t) {
        onFailed(t.getMessage());
        Log.e(TAG, "onFailure: " + t.getMessage());
    }

    public abstract void onReceived(T body, String message);
    public abstract void onError(String message, int code);
    public abstract void onFailed(String message);
}
