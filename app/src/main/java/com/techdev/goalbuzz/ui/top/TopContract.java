package com.techdev.goalbuzz.ui.top;

import com.techdev.goalbuzz.model.League;
import com.techdev.goalbuzz.model.top.Scorer;
import com.techdev.goalbuzz.core.presentation.base.IBasePresenter;
import com.techdev.goalbuzz.core.presentation.base.IBaseView;

import java.util.List;

public class TopContract {

    public interface View extends IBaseView {
        void onResponse(List<Scorer> scorerList, String message);
        void onLeagueView(List<League> leagueList);
    }

    public interface Presenter extends IBasePresenter<View> {
        void onTopScorerApiTask(String leagueId);
        void onLoadLeague();
    }
}
