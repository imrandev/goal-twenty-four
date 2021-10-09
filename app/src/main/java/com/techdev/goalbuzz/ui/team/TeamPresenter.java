package com.techdev.goalbuzz.ui.team;

import com.techdev.goalbuzz.model.team.Team;
import com.techdev.goalbuzz.model.team.TeamInfo;
import com.techdev.goalbuzz.core.network.response.EnqueueResponse;
import com.techdev.goalbuzz.ui.base.BasePresenter;

import java.util.List;

import javax.inject.Inject;

import retrofit2.Call;

public class TeamPresenter extends BasePresenter<TeamContract.View> implements TeamContract.Presenter {

    private Call<TeamInfo> teamInfoCall;

    @Inject
    TeamPresenter() {

    }

    @Override
    public void onTeamApiTask(String name) {

        view.showLoader();

        teamInfoCall = retrofitDbClient.getRepository().getSingleTeamInfo(name);

        teamInfoCall.enqueue(new EnqueueResponse<TeamInfo>() {
            @Override
            public void onReceived(TeamInfo body, String message) {
                List<Team> teams = body.getTeams();
                Team team = teams != null ? teams.get(0) : null;
                if (view != null){
                    view.onResponse(team, message);
                    view.hideLoader();
                }
            }

            @Override
            public void onError(String message, int code) {
                if (view != null){
                    view.onResponse(null, message);
                    view.hideLoader();
                }
            }

            @Override
            public void onFailed(String message) {
                if (view != null){
                    view.onResponse(null, message);
                    view.hideLoader();
                }
            }
        });
    }

    @Override
    public void onDetach() {
        if (teamInfoCall != null){
            if(teamInfoCall.isExecuted()){
                teamInfoCall.cancel();
                if (teamInfoCall.isCanceled()){
                    view.hideLoader();
                    teamInfoCall = null;
                }
            }
        }
        super.onDetach();
    }
}
