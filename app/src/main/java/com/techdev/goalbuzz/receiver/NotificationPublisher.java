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
import com.techdev.goalbuzz.core.datasource.local.db.entities.Match;
import com.techdev.goalbuzz.core.util.AppExecutors;
import com.techdev.goalbuzz.core.datasource.local.db.database.DatabaseManager;
import com.techdev.goalbuzz.featureMain.presentation.MainActivity;
import com.techdev.goalbuzz.core.util.Constant;
import com.techdev.goalbuzz.core.util.MatchUtil;

import java.util.Random;

public class NotificationPublisher extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        Bundle extras = intent.getBundleExtra(Constant.BROADCAST_SERIALIZABLE_BUNDLE_EXTRA);

        if (extras != null){
            com.techdev.goalbuzz.featureMain.domain.models.Match match = (com.techdev.goalbuzz.featureMain.domain.models.Match) extras.getSerializable(Constant.BROADCAST_SERIALIZABLE_BUNDLE_EXTRA);

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
                Match result = DatabaseManager.getInstance(context).resultDao().findById(match.getId());
                if (result != null){
                    DatabaseManager.getInstance(context).resultDao().delete(result);
                }
            });
        }
    }
}
