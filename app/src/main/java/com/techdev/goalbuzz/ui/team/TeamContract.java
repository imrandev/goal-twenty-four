package com.techdev.goalbuzz.ui.team;

import com.techdev.goalbuzz.model.team.Team;
import com.techdev.goalbuzz.ui.base.IBasePresenter;
import com.techdev.goalbuzz.ui.base.IBaseView;

public class TeamContract {

    public interface View extends IBaseView {
        void onResponse(Team team, String message);
    }

    public interface Presenter extends IBasePresenter<View> {
        void onTeamApiTask(String name);
    }
}
