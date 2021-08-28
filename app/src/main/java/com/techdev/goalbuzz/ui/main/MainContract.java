package com.techdev.goalbuzz.ui.main;

import com.techdev.goalbuzz.model.League;
import com.techdev.goalbuzz.model.Query;
import com.techdev.goalbuzz.model.live.Match;
import com.techdev.goalbuzz.ui.base.IBasePresenter;
import com.techdev.goalbuzz.ui.base.IBaseView;

import java.util.List;

public class MainContract {

    public interface View extends IBaseView {
        void onLiveView(List<Match> matchList, String message, int count);
        void onUpcomingView(List<Match> matchList, String message, int count);
        void onFinishedView(List<Match> matchList, String message, int count);
        void onTeamView(List<Query> teamList, String message, int count);
        void onLeagueView(List<League> leagueList);
    }

    public interface Presenter extends IBasePresenter<View> {
        void onMatchApiTask();
        void onLoadLeague();
        void setReminder(Match match);
        void sendCrashReport(String errorMessage);
        boolean hasScheduled(Match match);
    }
}
