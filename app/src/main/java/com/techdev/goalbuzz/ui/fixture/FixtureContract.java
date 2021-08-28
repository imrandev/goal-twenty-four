package com.techdev.goalbuzz.ui.fixture;

import com.techdev.goalbuzz.model.League;
import com.techdev.goalbuzz.model.fixture.Match;
import com.techdev.goalbuzz.ui.base.IBasePresenter;
import com.techdev.goalbuzz.ui.base.IBaseView;

import java.util.List;

public class FixtureContract {
    public interface View extends IBaseView {
        void onResponse(List<Match> matchList, String message);
        void onLeagueView(List<League> leagueList);
        void loadAutocomplete(List<String> teams);
    }

    public interface Presenter extends IBasePresenter<View> {
        void onFixtureApiTask(String leagueId);
        void onLoadLeague();
        boolean isExists(Match match, List<String> teamList);
        void addTeamSuggestions(List<Match> matches);
    }
}
