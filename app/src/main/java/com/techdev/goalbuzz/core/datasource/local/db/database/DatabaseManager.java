package com.techdev.goalbuzz.core.datasource.local.db.database;

import android.content.Context;
import android.util.Log;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.techdev.goalbuzz.core.datasource.local.db.dao.AdmobDao;
import com.techdev.goalbuzz.core.datasource.local.db.dao.MatchDao;
import com.techdev.goalbuzz.core.datasource.local.db.entities.Admob;
import com.techdev.goalbuzz.core.datasource.local.db.entities.Match;

@Database(
        entities = {Match.class, Admob.class},
        version = 2,
        exportSchema = false
)
public abstract class DatabaseManager extends RoomDatabase {

    private static final String LOG_TAG = DatabaseManager.class.getSimpleName();
    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "match_results";
    private static DatabaseManager sInstance;

    public static DatabaseManager getInstance(Context context) {
        if (sInstance == null) {
            synchronized (LOCK) {
                Log.d(LOG_TAG, "Creating new database instance");
                sInstance = Room.databaseBuilder(
                        context.getApplicationContext(), DatabaseManager.class, DatabaseManager.DATABASE_NAME
                ).fallbackToDestructiveMigration().build();
            }
        }
        Log.d(LOG_TAG, "Getting the database instance");
        return sInstance;
    }

    public abstract MatchDao resultDao();
    public abstract AdmobDao admobDao();
}
