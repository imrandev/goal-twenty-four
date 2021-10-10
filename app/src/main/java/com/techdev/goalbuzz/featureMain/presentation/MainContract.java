package com.techdev.goalbuzz.featureMain.presentation;

import android.widget.CheckBox;

import com.techdev.goalbuzz.model.League;
import com.techdev.goalbuzz.featureMain.domain.models.Match;
import com.techdev.goalbuzz.core.presentation.base.IBasePresenter;
import com.techdev.goalbuzz.core.presentation.base.IBaseView;

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
