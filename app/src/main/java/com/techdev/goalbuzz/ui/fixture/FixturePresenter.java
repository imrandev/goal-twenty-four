package com.techdev.goalbuzz.ui.fixture;

import android.os.Handler;
import android.util.Log;

import com.techdev.goalbuzz.model.fixture.Fixtures;
import com.techdev.goalbuzz.model.fixture.Match;
import com.techdev.goalbuzz.network.response.EnqueueResponse;
import com.techdev.goalbuzz.ui.base.BasePresenter;
import com.techdev.goalbuzz.util.Constant;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import retrofit2.Call;

public class FixturePresenter extends BasePresenter<FixtureContract.View> implements FixtureContract.Presenter {

    private Call<Fixtures> fixturesCall;

    private static final String TAG = "FixturePresenter";

    @Inject
    FixturePresenter() {

    }

    @Override
    public void onFixtureApiTask(String leagueId) {
        view.showLoader();
        fixturesCall = retrofitClient.getRepository().getFixturesByLeagueId(leagueId, Constant.UPCOMING_MATCH);
        fixturesCall.enqueue(new EnqueueResponse<Fixtures>() {
            @Override
            public void onReceived(Fixtures body, String message) {
                List<Match> matchList = body.getMatches();
                if (view != null){
                    if (matchList != null && matchList.size() > 0){
                        view.onResponse(body.getMatches(), message);
                    } else {
                        view.onResponse(new ArrayList<>(2), message);
                    }
                    view.hideLoader();
                }
            }

            @Override
            public void onError(String message, int code) {
                if (view != null) {
                    view.onResponse(new ArrayList<>(2), message);
                    view.hideLoader();
                }
            }

            @Override
            public void onFailed(String message) {
                if (view != null) {
                    view.onResponse(new ArrayList<>(2), message);
                    view.hideLoader();
                }
            }
        });
    }

    @Override
    public void onLoadLeague() {

    }

    @Override
    public boolean isExists(Match match, List<String> teamList) {
        if (match.getHomeTeam() == null || match.getHomeTeam().getName() == null
                || match.getHomeTeam().getName().isEmpty() || teamList.isEmpty()) return false;
        boolean exists = false;
        String home = match.getHomeTeam().getName();
        for (String team : teamList) {
            if (team.equals(home)){
                exists = true;
                break;
            }
        }
        Log.d(TAG, "isExists: " + exists);
        return exists;
    }

    @Override
    public void addTeamSuggestions(List<Match> matches) {
        List<String> teamList = new ArrayList<>(2);
        new Handler().post(() -> {
            for (Match match: matches){
                if (!isExists(match, teamList))
                    teamList.add(match.getHomeTeam().getName());
            }
            view.loadAutocomplete(teamList);
        });
    }

    @Override
    public void onDetach() {
        if (fixturesCall != null){
            if (fixturesCall.isExecuted()){
                fixturesCall.cancel();
                if (fixturesCall.isCanceled()){
                    view.hideLoader();
                    fixturesCall = null;
                }
            }
        }
        super.onDetach();
    }
}
