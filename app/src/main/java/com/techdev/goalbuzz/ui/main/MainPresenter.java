package com.techdev.goalbuzz.ui.main;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.CheckBox;

import com.techdev.goalbuzz.model.League;
import com.techdev.goalbuzz.model.Query;
import com.techdev.goalbuzz.model.live.Live;
import com.techdev.goalbuzz.model.live.Match;
import com.techdev.goalbuzz.model.live.Score;
import com.techdev.goalbuzz.network.response.EnqueueResponse;
import com.techdev.goalbuzz.receiver.NotificationPublisher;
import com.techdev.goalbuzz.room.database.AppExecutors;
import com.techdev.goalbuzz.room.database.RoomManager;
import com.techdev.goalbuzz.room.model.Result;
import com.techdev.goalbuzz.ui.base.BasePresenter;
import com.techdev.goalbuzz.util.Constant;
import com.techdev.goalbuzz.util.DateFormatter;
import com.techdev.goalbuzz.util.MatchUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import javax.inject.Inject;

import retrofit2.Call;

public class MainPresenter extends BasePresenter<MainContract.View> implements MainContract.Presenter {

    private Call<Live> liveCall;

    @Inject
    MainPresenter() {

    }

    @Override
    public void onMatchApiTask() {
        view.showLoader();
        liveCall = retrofitClient.getRepository().getMatches();
        liveCall.enqueue(new EnqueueResponse<Live>() {
            @Override
            public void onReceived(Live body, String message) {
                List<Match> matchList = body.getMatches();
                List<Match> mLiveMatches = new ArrayList<>(2);
                List<Match> mUpcomingMatches = new ArrayList<>(2);
                List<Match> mFinishedMatches = new ArrayList<>(2);
                if (matchList != null){
                    if (matchList.size() > 0){
                        new Handler().post(() -> {
                            for (Match m : matchList) {
                                if (m.getStatus().equals(Constant.LIVE_MATCH)){
                                    mLiveMatches.add(m);
                                } else if (m.getStatus().equals(Constant.UPCOMING_MATCH)
                                        && !DateFormatter.getInstance().hasTimeCrossedYet(m.getUtcDate())){
                                    AppExecutors.getInstance().diskIO().execute(() -> {
                                        // check if match already in the schedule
                                        Result result = roomManager.resultDao().findById(m.getId());
                                        m.setHasScheduled(result!= null);
                                    });
                                    mUpcomingMatches.add(m);
                                } else {
                                    mFinishedMatches.add(m);
                                }
                            }
                            if (view != null) {
                                if (mLiveMatches.size() > 0){
                                    view.onChangeMarquee(generateMarqueeText(mLiveMatches, Constant.LIVE_MATCH));
                                } else if (mFinishedMatches.size() > 0){
                                    view.onChangeMarquee(generateMarqueeText(mFinishedMatches, Constant.FINISHED_MATCH));
                                } else {
                                    view.onChangeMarquee(generateMarqueeText(mUpcomingMatches, Constant.UPCOMING_MATCH));
                                }
                                view.onLiveView(mLiveMatches, message, mLiveMatches.size());
                                view.onUpcomingView(mUpcomingMatches, message, mUpcomingMatches.size());
                                view.onFinishedView(mFinishedMatches, message, mFinishedMatches.size());
                                view.hideLoader();
                            }
                        });
                    } else {
                        sendEmptyApiDataToView(new ArrayList<>(2), message);
                    }
                } else {
                    sendEmptyApiDataToView(new ArrayList<>(2), message);
                }
            }

            @Override
            public void onError(String message, int code) {
                sendEmptyApiDataToView(new ArrayList<>(2), message);
            }

            @Override
            public void onFailed(String message) {
                sendEmptyApiDataToView(new ArrayList<>(2), message);
            }
        });
    }

    @Override
    public void onLoadLeague() {
        List<League> leagueList = new ArrayList<>(2);
        int index = 0;
        for (String name : resourceUtils.getLeagueNames()){
            League league = new League();
            league.setName(name);
            league.setDrawableRes(resourceUtils.getLeagueIcons()[index]);
            leagueList.add(league);
            index++;
        }
        view.onLeagueView(leagueList);
    }

    @Override
    public void scheduleMatch(Match match, CheckBox checkBox) {
        AtomicBoolean success = new AtomicBoolean(false);
        AppExecutors.getInstance().diskIO().execute(() -> {
            // check if match already in the schedule
            Result result = roomManager.resultDao().findById(match.getId());
            if (result == null){
                // set match time as calender
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(DateFormatter.getInstance().getDateTime(match.getUtcDate()));
                /*calendar.setTime(DateFormatter.getInstance().getDateTime("2021-01-25T06:44:00Z"));*/
                calendar.add(Calendar.MINUTE, -5);

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
                roomManager.resultDao().insert(new Result(match));
                success.set(true);

                AppExecutors.getInstance().mainThread().execute(() -> {
                    view.updateCheckBox(checkBox, true);
                    // showing toast
                    view.showMessage("Match scheduled for notification");
                });
            } else {
                // cancel notification
                cancelSchedule(match.getId(), checkBox);
            }
        });
        success.get();
    }

    @Override
    public void cancelSchedule(int eventId, CheckBox checkBox) {
        Intent intent = new Intent(context, NotificationPublisher.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, eventId, intent, 0);
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        am.cancel(pendingIntent);
        pendingIntent.cancel();
        AppExecutors.getInstance().diskIO().execute(() -> {
            Result result = RoomManager.getInstance(context).resultDao().findById(eventId);
            if (result != null){
                RoomManager.getInstance(context).resultDao().delete(result);
                AppExecutors.getInstance().mainThread().execute(() -> {
                    view.updateCheckBox(checkBox, false);
                    view.showMessage("Canceled Schedule");
                });
            }
        });
    }

    @Override
    public void sendCrashReport(String errorMessage) {

    }

    @Override
    public boolean hasScheduled(Match match) {
        AtomicReference<Result> result = new AtomicReference<>();
        AppExecutors.getInstance().diskIO().execute(() -> {
            // check if match already in the schedule
            result.set(roomManager.resultDao().findById(match.getId()));
        });
        return result.get() != null;
    }

    private void sendEmptyApiDataToView(List<Match> matchList, String message){
        if (view != null) {
            view.onLiveView(matchList, message, 0);
            view.onUpcomingView(matchList, message, 0);
            view.onLiveView(matchList, message, 0);
            view.onTeamView(new ArrayList<>(), message, 0);
            view.hideLoader();
        }
    }

    @Override
    public void onDetach() {
        if (liveCall != null){
            if(liveCall.isExecuted()){
                liveCall.cancel();
                if (liveCall.isCanceled()){
                    view.hideLoader();
                    liveCall = null;
                }
            }
        }
        super.onDetach();
    }

    private boolean isExists(Match match, List<Query> teamList){
        if (!teamList.isEmpty()){
            for (Query query : teamList) {
                String home = match.getHomeTeam().getName();
                String away = match.getAwayTeam().getName();
                if (query.getName().equals(home) || query.getName().equals(away)) {
                    return true;
                }
            }
        }
        return false;
    }

    private String generateMarqueeText(List<Match> matchList, String type){
        StringBuilder builder = new StringBuilder();
        for (int i=0; i<matchList.size(); i++) {
            Match match = matchList.get(i);
            String endIndent = i < matchList.size() - 1 ? "|" : "";
            String marqueeText = "";
            if (type.equals(Constant.UPCOMING_MATCH)){
                String playTime = MatchUtil.getInstance().getPlayTime(match);
                marqueeText = String.format("Matchday %s • %s Vs %s, Match Starts in %s %s", match.getMatchday(),
                        match.getHomeTeam().getName(), match.getAwayTeam().getName(), playTime, endIndent);
            } else {
                Score score = match.getScore();
                if (score != null){
                    String tag = type.equals(Constant.LIVE_MATCH) ? "▣ Live " : "";
                    int home = score.getPenalties().getHomeTeam() != null ?
                            score.getPenalties().getHomeTeam() : score.getExtraTime().getHomeTeam() != null ?
                            score.getExtraTime().getHomeTeam() : score.getFullTime().getHomeTeam() != null ?
                            score.getFullTime().getHomeTeam() : score.getHalfTime().getHomeTeam() != null ?
                            score.getHalfTime().getHomeTeam() : 0;
                    int away = score.getPenalties().getAwayTeam() != null ?
                            score.getPenalties().getAwayTeam() : score.getExtraTime().getAwayTeam() != null ?
                            score.getExtraTime().getAwayTeam() : score.getFullTime().getAwayTeam() != null ?
                            score.getFullTime().getAwayTeam() : score.getHalfTime().getAwayTeam() != null ?
                            score.getHalfTime().getAwayTeam() : 0;
                    marqueeText = String.format("%sMatchday %s • %s %s - %s %s %s", tag, match.getMatchday(),
                            match.getHomeTeam().getName(), home, match.getAwayTeam().getName(), away, endIndent);
                }
            }
            builder.append(marqueeText);
        }
        return builder.toString();
    }
}
