package com.techdev.goalbuzz.ui.main;

import android.widget.CheckBox;

import com.techdev.goalbuzz.model.League;
import com.techdev.goalbuzz.model.Query;
import com.techdev.goalbuzz.model.live.Match;
import com.techdev.goalbuzz.ui.base.IBasePresenter;
import com.techdev.goalbuzz.ui.base.IBaseView;

import java.util.List;

public class MainContract {

    public interface View extends IBaseView {
        void showLiveView(List<Match> matchList, String message, int count);
        void showUpcomingView(List<Match> matchList, String message, int count);
        void showFinishedView(List<Match> matchList, String message, int count);
        void showListOfTopLeagues(List<League> leagueList);
        void showMarquee(String text);
        void onUpdateScheduleState(CheckBox checkBox, boolean scheduled);
    }

    public interface Presenter extends IBasePresenter<View> {
        void executeMatchApi();
        void loadTopLeagues();
        void toggleSchedule(Match match, CheckBox checkBox);
    }
}
