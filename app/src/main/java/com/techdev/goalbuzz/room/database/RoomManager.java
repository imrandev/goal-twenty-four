package com.techdev.goalbuzz.room.database;

import android.content.Context;
import android.util.Log;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.techdev.goalbuzz.room.dao.AdmobDao;
import com.techdev.goalbuzz.room.dao.ResultDao;
import com.techdev.goalbuzz.room.model.Admob;
import com.techdev.goalbuzz.room.model.Result;

@Database(
        entities = {Result.class, Admob.class},
        version = 2,
        exportSchema = false
)
public abstract class RoomManager extends RoomDatabase {

    private static final String LOG_TAG = RoomManager.class.getSimpleName();
    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "match_results";
    private static RoomManager sInstance;

    public static RoomManager getInstance(Context context) {
        if (sInstance == null) {
            synchronized (LOCK) {
                Log.d(LOG_TAG, "Creating new database instance");
                sInstance = Room.databaseBuilder(
                        context.getApplicationContext(), RoomManager.class, RoomManager.DATABASE_NAME
                ).fallbackToDestructiveMigration().build();
            }
        }
        Log.d(LOG_TAG, "Getting the database instance");
        return sInstance;
    }

    public abstract ResultDao resultDao();
    public abstract AdmobDao admobDao();
}
