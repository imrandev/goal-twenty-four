package com.techdev.goalbuzz.core.datasource.local.prefs;

import android.content.Context;
import android.content.SharedPreferences;

import com.techdev.goalbuzz.core.util.Constant;

public class PrefManager {
    private final SharedPreferences sharedPreferences;
    private static PrefManager instance;

    private PrefManager(Context context) {
        sharedPreferences = context.getSharedPreferences(
                Constant.PREF_BASE_NAME, Context.MODE_MULTI_PROCESS);
    }

    public static PrefManager getInstance(Context appContext) {
        if (instance == null) {
            synchronized (PrefManager.class) {
                if (instance == null) {
                    instance = new PrefManager(appContext);
                }
            }
        }
        return instance;
    }

    public void putInt(int i, String s) {
        sharedPreferences.edit().putInt(s, i).apply();
    }

    public int getInt(String s) {
        return sharedPreferences.getInt(s, 0);
    }

    public void putBoolean(boolean bool, String s) {
        sharedPreferences.edit().putBoolean(s, bool).apply();
    }

    public boolean getBoolean(String s) {
        return sharedPreferences.getBoolean(s, false);
    }

    public void putString(String s, String s1){
        sharedPreferences.edit().putString(s, s1).apply();
    }

    public boolean contains(String key) {
        return sharedPreferences.contains(key);
    }

    public void remove(String key) {
        sharedPreferences.edit().remove(key).apply();
    }

    public void clear(){
        sharedPreferences.edit().clear().apply();
    }
}
