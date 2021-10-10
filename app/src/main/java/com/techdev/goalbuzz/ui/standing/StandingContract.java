package com.techdev.goalbuzz.ui.standing;

import com.techdev.goalbuzz.model.League;
import com.techdev.goalbuzz.model.point.Standing;
import com.techdev.goalbuzz.core.presentation.base.IBasePresenter;
import com.techdev.goalbuzz.core.presentation.base.IBaseView;

import java.util.List;

public class StandingContract {

    public interface View extends IBaseView {
        void onResponse(List<Standing> standingList, String message);
        void onLeagueView(List<League> leagueList);
    }

    public interface Presenter extends IBasePresenter<View> {
        void onStandingApiTask(String leagueId);
        void onLoadLeague();
    }
}
