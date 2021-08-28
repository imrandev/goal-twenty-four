package com.techdev.goalbuzz.ui.standing;

import com.techdev.goalbuzz.model.point.PointTable;
import com.techdev.goalbuzz.model.point.Standing;
import com.techdev.goalbuzz.network.response.EnqueueResponse;
import com.techdev.goalbuzz.ui.base.BasePresenter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import retrofit2.Call;

public class StandingPresenter extends BasePresenter<StandingContract.View> implements StandingContract.Presenter {

    private Call<PointTable> pointTableCall;

    @Inject
    StandingPresenter() {
    }

    @Override
    public void onStandingApiTask(String leagueId) {
        view.showLoader();
        pointTableCall = retrofitClient.getRepository().getStandingsByLeague(leagueId);

        pointTableCall.enqueue(new EnqueueResponse<PointTable>() {
            @Override
            public void onReceived(PointTable body, String message) {
                List<Standing> standingList = body.getStandings();
                if (standingList != null){
                    if (standingList.size() > 0){
                        if (view != null) {
                            view.onResponse(body.getStandings(), message);
                            view.hideLoader();
                            return;
                        }
                    }
                }
                if (view != null){
                    view.onResponse(new ArrayList<>(2), message);
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
    public void onDetach() {
        if (pointTableCall != null){
            if (pointTableCall.isExecuted()){
                pointTableCall.cancel();
                if (pointTableCall.isCanceled()){
                    view.hideLoader();
                    pointTableCall = null;
                }
            }
        }
        super.onDetach();
    }
}
