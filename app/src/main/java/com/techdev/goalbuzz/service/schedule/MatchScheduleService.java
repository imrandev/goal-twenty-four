package com.techdev.goalbuzz.service.schedule;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.techdev.goalbuzz.model.live.Match;
import com.techdev.goalbuzz.receiver.NotificationPublisher;
import com.techdev.goalbuzz.room.database.AppExecutors;
import com.techdev.goalbuzz.room.database.RoomManager;
import com.techdev.goalbuzz.room.model.Result;
import com.techdev.goalbuzz.util.Constant;
import com.techdev.goalbuzz.util.DateFormatter;

import java.util.Calendar;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class MatchScheduleService implements ScheduleService<Match> {

    private static MatchScheduleService instance;

    public static final int SCHEDULE_TIME_BEFORE_MATCH_START = -5;
    
    private static final String TAG = "MatchScheduleService";

    public static MatchScheduleService getInstance(){
        if (instance == null){
            instance = new MatchScheduleService();
        }
        return instance;
    }

    @Override
    public boolean schedule(Match match, Context context) {
        AtomicBoolean _flag = new AtomicBoolean(false);
        AppExecutors.getInstance().diskIO().execute(() -> {
            Result result = RoomManager.getInstance(context).resultDao().findById(match.getId());
            if (result == null) {
                // set match time as calender
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(DateFormatter.getInstance().getDateTime(match.getUtcDate()));
                /*calendar.setTime(DateFormatter.getInstance().getDateTime("2021-01-25T06:44:00Z"));*/
                calendar.add(Calendar.MINUTE, SCHEDULE_TIME_BEFORE_MATCH_START);
                // create bundle for passing data into receiver
                Bundle extras = new Bundle();
                extras.putSerializable(Constant.BROADCAST_SERIALIZABLE_BUNDLE_EXTRA, match);
                // start broadcast receiver and pass the bundle data
                Intent intentReceiver = new Intent(context, NotificationPublisher.class);
                intentReceiver.putExtra(Constant.BROADCAST_SERIALIZABLE_BUNDLE_EXTRA, extras);
                // set scheduling notification for upcoming match
                PendingIntent pendingIntent = PendingIntent.getBroadcast(context, match.getId(), intentReceiver, PendingIntent.FLAG_ONE_SHOT);
                AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
                // after scheduling for notification, insert the record into local db
                // inside a background thread for performing the room task
                RoomManager.getInstance(context).resultDao().insert(new Result(match));
                result = RoomManager.getInstance(context).resultDao().findById(match.getId());
            }
            _flag.compareAndSet(true, result != null);
        });
        return _flag.get();
    }

    @Override
    public void schedule(Match match, Context context, OnScheduleCallback onScheduleCallback) {
        AppExecutors.getInstance().diskIO().execute(() -> {
            // set match time as calender
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(DateFormatter.getInstance().getDateTime(match.getUtcDate()));
            /*calendar.setTime(DateFormatter.getInstance().getDateTime("2021-01-25T06:44:00Z"));*/
            calendar.add(Calendar.MINUTE, SCHEDULE_TIME_BEFORE_MATCH_START);
            // create bundle for passing data into receiver
            Bundle extras = new Bundle();
            extras.putSerializable(Constant.BROADCAST_SERIALIZABLE_BUNDLE_EXTRA, match);
            // start broadcast receiver and pass the bundle data
            Intent intentReceiver = new Intent(context, NotificationPublisher.class);
            intentReceiver.putExtra(Constant.BROADCAST_SERIALIZABLE_BUNDLE_EXTRA, extras);
            // set scheduling notification for upcoming match
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, match.getId(), intentReceiver, PendingIntent.FLAG_ONE_SHOT);
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
            // after scheduling for notification, insert the record into local db
            // inside a background thread for performing the room task
            RoomManager.getInstance(context).resultDao().insert(new Result(match));
            onScheduleCallback.onScheduled();
        });
    }

    @Override
    public void scheduleOrCancel(Match match, Context context, OnScheduleCallback onScheduleCallback, OnCancelCallback onCancelCallback) {
        AppExecutors.getInstance().diskIO().execute(() -> {
            // check if match already in the schedule
            Result result = RoomManager.getInstance(context).resultDao().findById(match.getId());
            if (result == null){
                schedule(match, context, onScheduleCallback);
            } else {
                cancel(match.getId(), context, onCancelCallback);
            }
        });
    }

    @Override
    public boolean cancel(int eventId, Context context) {
        AtomicBoolean _flag = new AtomicBoolean();
        Intent intent = new Intent(context, NotificationPublisher.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, eventId, intent, 0);
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        am.cancel(pendingIntent);
        pendingIntent.cancel();
        AppExecutors.getInstance().diskIO().execute(() -> {
            Result result = RoomManager.getInstance(context).resultDao().findById(eventId);
            if (result != null){
                RoomManager.getInstance(context).resultDao().delete(result);
                _flag.set(RoomManager.getInstance(context).resultDao().findById(eventId) == null);
            }
        });
        return _flag.get();
    }

    @Override
    public void cancel(int eventId, Context context, OnCancelCallback onCancelCallback) {
        Intent intent = new Intent(context, NotificationPublisher.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, eventId, intent, 0);
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        am.cancel(pendingIntent);
        pendingIntent.cancel();
        AppExecutors.getInstance().diskIO().execute(() -> {
            Result result = RoomManager.getInstance(context).resultDao().findById(eventId);
            if (result != null){
                RoomManager.getInstance(context).resultDao().delete(result);
                onCancelCallback.onCanceled();
            }
        });
    }
}
