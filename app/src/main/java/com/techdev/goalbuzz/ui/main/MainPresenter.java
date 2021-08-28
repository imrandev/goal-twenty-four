package com.techdev.goalbuzz.ui.main;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.techdev.goalbuzz.model.League;
import com.techdev.goalbuzz.model.Query;
import com.techdev.goalbuzz.model.live.Live;
import com.techdev.goalbuzz.model.live.Match;
import com.techdev.goalbuzz.network.response.EnqueueResponse;
import com.techdev.goalbuzz.receiver.NotificationPublisher;
import com.techdev.goalbuzz.room.database.AppExecutors;
import com.techdev.goalbuzz.room.model.Result;
import com.techdev.goalbuzz.ui.base.BasePresenter;
import com.techdev.goalbuzz.util.Constant;
import com.techdev.goalbuzz.util.DateFormatter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;
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
                List<Query> teamList = new ArrayList<>(2);

                if (matchList != null){
                    if (matchList.size() > 0){
                        new Handler().post(() -> {
                            for (Match m : matchList) {
                                if (m.getStatus().equals(Constant.LIVE_MATCH)){
                                    mLiveMatches.add(m);
                                } else if (m.getStatus().equals(Constant.UPCOMING_MATCH)
                                        && !DateFormatter.getInstance().hasTimeCrossedYet(m.getUtcDate())){
                                    mUpcomingMatches.add(m);
                                } else {
                                    mFinishedMatches.add(m);
                                }
                                /*if (!isExists(m, teamList)) {
                                    if (m.getCompetition().getName().equals("UEFA Champions League")
                                            || m.getCompetition().getName().equals("Premier League")
                                            || m.getCompetition().getName().equals("Primera Division")
                                            || m.getCompetition().getName().equals("Serie A")) {
                                        HomeTeam homeTeam = m.getHomeTeam();
                                        AwayTeam awayTeam = m.getAwayTeam();
                                        Query homeQuery = new Query(homeTeam.getId(), homeTeam.getName());
                                        Query awayQuery = new Query(awayTeam.getId(), awayTeam.getName());
                                        teamList.add(homeQuery);
                                        teamList.add(awayQuery);
                                    }
                                }*/
                            }

                            if (view != null) {
                                view.onLiveView(mLiveMatches, message, mLiveMatches.size());
                                view.onUpcomingView(mUpcomingMatches, message, mUpcomingMatches.size());
                                view.onFinishedView(mFinishedMatches, message, mFinishedMatches.size());
//                                view.onTeamView(teamList, message, teamList.size());
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
    public void setReminder(Match match) {
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
                PendingIntent pendingIntent = PendingIntent.getBroadcast(context, new Random().nextInt(10000), intentReceiver, PendingIntent.FLAG_ONE_SHOT);
                AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);

                // after scheduling for notification, insert the record into local db
                // inside a background thread for performing the room task
                roomManager.resultDao().insert(new Result(match));
                success.set(true);
            }
            // showing toast
            AppExecutors.getInstance().mainThread().execute(() -> view.showMessage(result == null ?
                    "Match successfully scheduled for notification" : "Match already scheduled for notification"));
        });
        success.get();
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
}
