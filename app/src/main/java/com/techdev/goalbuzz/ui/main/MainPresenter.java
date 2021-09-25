package com.techdev.goalbuzz.ui.main;
import android.os.Handler;
import android.widget.CheckBox;

import com.techdev.goalbuzz.model.League;
import com.techdev.goalbuzz.model.live.Live;
import com.techdev.goalbuzz.model.live.Match;
import com.techdev.goalbuzz.network.response.EnqueueResponse;
import com.techdev.goalbuzz.room.database.AppExecutors;
import com.techdev.goalbuzz.room.model.Result;
import com.techdev.goalbuzz.service.marquee.MarqueeService;
import com.techdev.goalbuzz.service.match.MatchService;
import com.techdev.goalbuzz.service.schedule.MatchScheduleService;
import com.techdev.goalbuzz.service.schedule.ScheduleService;
import com.techdev.goalbuzz.ui.base.BasePresenter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import javax.inject.Inject;

import retrofit2.Call;

public class MainPresenter extends BasePresenter<MainContract.View> implements MainContract.Presenter {

    private Call<Live> liveCall;

    @Inject
    public MarqueeService<Match> marqueeService;

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
                if (matchList != null && matchList.size() > 0){
                    List<Match> mLiveMatches = MatchService.init(matchList).getLives();
                    List<Match> mUpcomingMatches = MatchService.init(matchList).getUpcoming(roomManager);
                    List<Match> mFinishedMatches = MatchService.init(matchList).getResults();
                    if (view != null) {
                        String marquee = marqueeService.get(mLiveMatches, mFinishedMatches, mUpcomingMatches);
                        if (!marquee.isEmpty()) {
                            new Handler().postDelayed(() -> view.onChangeMarquee(marquee), 1000);
                        }
                        view.onLiveView(mLiveMatches, message, mLiveMatches.size());
                        view.onUpcomingView(mUpcomingMatches, message, mUpcomingMatches.size());
                        view.onFinishedView(mFinishedMatches, message, mFinishedMatches.size());
                        view.hideLoader();
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
    public void toggleSchedule(Match match, CheckBox checkBox) {
        MatchScheduleService.getInstance().scheduleOrCancel(match, context, () -> AppExecutors.getInstance().mainThread().execute(() -> {
            view.updateCheckBox(checkBox, true);
            // showing toast
            view.showMessage("Match scheduled for notification");
        }), () -> AppExecutors.getInstance().mainThread().execute(() -> {
            view.updateCheckBox(checkBox, false);
            // showing toast
            view.showMessage("Canceled Schedule");
        }));
    }

    @Override
    public void cancelSchedule(int eventId, CheckBox checkBox) {
        MatchScheduleService.getInstance().cancel(eventId, context, new ScheduleService.OnCancelCallback() {
            @Override
            public void onCanceled() {
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
        if (liveCall != null && liveCall.isExecuted()){
            liveCall.cancel();
            if (liveCall.isCanceled()){
                view.hideLoader();
                liveCall = null;
            }
        }
        super.onDetach();
    }
}
