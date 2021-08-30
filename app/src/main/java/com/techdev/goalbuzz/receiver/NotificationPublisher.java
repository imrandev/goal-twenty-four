package com.techdev.goalbuzz.receiver;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.core.app.NotificationCompat;

import com.techdev.goalbuzz.R;
import com.techdev.goalbuzz.model.live.Match;
import com.techdev.goalbuzz.room.database.AppExecutors;
import com.techdev.goalbuzz.room.database.RoomManager;
import com.techdev.goalbuzz.room.model.Result;
import com.techdev.goalbuzz.ui.main.MainActivity;
import com.techdev.goalbuzz.util.Constant;
import com.techdev.goalbuzz.util.DateFormatter;
import com.techdev.goalbuzz.util.MatchUtil;

import java.util.Random;

public class NotificationPublisher extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        Bundle extras = intent.getBundleExtra(Constant.BROADCAST_SERIALIZABLE_BUNDLE_EXTRA);

        if (extras != null){
            Match match = (Match) extras.getSerializable(Constant.BROADCAST_SERIALIZABLE_BUNDLE_EXTRA);

            Intent notificationIntent = new Intent(context, MainActivity.class);
            PendingIntent pendingIntent
                    = PendingIntent.getActivity(context, new Random().nextInt(10000), notificationIntent, PendingIntent.FLAG_ONE_SHOT);

            String playTime = MatchUtil.getInstance().getPlayTime(match);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, Constant.CHANNEL_ID)
                    .setContentTitle(String.format("%s - Matchday %s", match.getCompetition().getName(), match.getMatchday()))
                    .setContentText("Don't forget to watch")
                    .setSmallIcon(R.mipmap.ic_launcher_round)
                    .setContentIntent(pendingIntent)
                    .setStyle(new NotificationCompat.BigTextStyle()
                            .bigText(String.format("%s Vs %s, starts in %s", match.getHomeTeam().getName(), match.getAwayTeam().getName(), playTime)));

            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context. NOTIFICATION_SERVICE ) ;
            Notification notification = builder.build();

            if (android.os.Build.VERSION. SDK_INT >= android.os.Build.VERSION_CODES. O ) {
                int importance = NotificationManager.IMPORTANCE_HIGH;
                NotificationChannel notificationChannel =
                        new NotificationChannel(Constant.CHANNEL_ID , "NOTIFICATION_CHANNEL_NAME" , importance) ;
                notificationManager.createNotificationChannel(notificationChannel);
            }
            notificationManager.notify(match.getId() , notification);
            AppExecutors.getInstance().diskIO().execute(() -> {
                Result result = RoomManager.getInstance(context).resultDao().findById(match.getId());
                if (result != null){
                    RoomManager.getInstance(context).resultDao().delete(result);
                }
            });
        }
    }
}
