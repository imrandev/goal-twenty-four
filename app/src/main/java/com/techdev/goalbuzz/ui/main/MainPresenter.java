package com.techdev.goalbuzz.ui.main;
import android.os.Handler;
import android.widget.CheckBox;

import com.techdev.goalbuzz.model.League;
import com.techdev.goalbuzz.model.live.Live;
import com.techdev.goalbuzz.model.live.Match;
import com.techdev.goalbuzz.network.response.EnqueueResponse;
import com.techdev.goalbuzz.room.database.AppExecutors;
import com.techdev.goalbuzz.service.marquee.MarqueeService;
import com.techdev.goalbuzz.service.match.MatchService;
import com.techdev.goalbuzz.service.schedule.MatchScheduleService;
import com.techdev.goalbuzz.ui.base.BasePresenter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import retrofit2.Call;

public class MainPresenter extends BasePresenter<MainContract.View> implements MainContract.Presenter {

    private Call<Live> requestLiveApi;

    @Inject
    public MarqueeService<Match> marqueeService;

    @Inject
    MainPresenter() {

    }

    @Override
    public void executeMatchApi() {
        view.showLoader();
        requestLiveApi = retrofitClient.getRepository().getMatches();
        requestLiveApi.enqueue(new EnqueueResponse<Live>() {
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
                            new Handler().postDelayed(() -> view.showMarquee(marquee), 1000);
                        }
                        view.showLiveView(mLiveMatches, message, mLiveMatches.size());
                        view.showUpcomingView(mUpcomingMatches, message, mUpcomingMatches.size());
                        view.showFinishedView(mFinishedMatches, message, mFinishedMatches.size());
                        view.hideLoader();
                    }
                } else {
                    passEmptyDataToView(new ArrayList<>(2), message);
                }
            }

            @Override
            public void onError(String message, int code) {
                passEmptyDataToView(new ArrayList<>(2), message);
            }

            @Override
            public void onFailed(String message) {
                passEmptyDataToView(new ArrayList<>(2), message);
            }
        });
    }

    @Override
    public void loadTopLeagues() {
        List<League> leagueList = new ArrayList<>(2);
        int index = 0;
        for (String name : resourceUtils.getNameOfLeagues()){
            League league = new League();
            league.setName(name);
            league.setDrawableRes(resourceUtils.getLeagueIcons()[index]);
            leagueList.add(league);
            index++;
        }
        view.showListOfTopLeagues(leagueList);
    }

    @Override
    public void toggleSchedule(Match match, CheckBox checkBox) {
        MatchScheduleService.getInstance().scheduleOrCancel(match, context, () -> AppExecutors.getInstance().mainThread().execute(() -> {
            view.onUpdateScheduleState(checkBox, true);
            // showing toast
            view.showMessage("Match scheduled for notification");
        }), () -> AppExecutors.getInstance().mainThread().execute(() -> {
            view.onUpdateScheduleState(checkBox, false);
            // showing toast
            view.showMessage("Canceled Schedule");
        }));
    }


    /*
    * If network failed or got any errors from server side
    * presenter will send some empty values to view
    * then view will do the corresponding thing
    * */
    private void passEmptyDataToView(List<Match> matchList, String message){
        if (view != null) {
            view.showLiveView(matchList, message, 0);
            view.showUpcomingView(matchList, message, 0);
            view.showLiveView(matchList, message, 0);
            view.hideLoader();
        }
    }

    @Override
    public void onDetach() {
        if (requestLiveApi != null && requestLiveApi.isExecuted()){
            requestLiveApi.cancel();
            if (requestLiveApi.isCanceled()){
                view.hideLoader();
                requestLiveApi = null;
            }
        }
        super.onDetach();
    }
}
