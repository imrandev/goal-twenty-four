package com.techdev.goalbuzz.ui.top;

import com.techdev.goalbuzz.model.top.Scorer;
import com.techdev.goalbuzz.model.top.TopScorer;
import com.techdev.goalbuzz.network.response.EnqueueResponse;
import com.techdev.goalbuzz.ui.base.BasePresenter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import retrofit2.Call;

public class TopPresenter extends BasePresenter<TopContract.View> implements TopContract.Presenter {

    private Call<TopScorer> topScorerCall;

    @Inject
    TopPresenter() {

    }

    @Override
    public void onTopScorerApiTask(String leagueId) {
        view.showLoader();
        topScorerCall = retrofitClient.getRepository().getTopScorerInfoById(leagueId);

        topScorerCall.enqueue(new EnqueueResponse<TopScorer>() {
            @Override
            public void onReceived(TopScorer body, String message) {
                List<Scorer> scorerList = body.getScorers();
                if (scorerList != null){
                    if (scorerList.size() > 0){
                        if (view != null){
                            view.onResponse(scorerList, message);
                            view.hideLoader();
                        }
                        return;
                    }
                }
                if (view != null) {
                    view.onResponse(new ArrayList<>(), message);
                    view.hideLoader();
                }
            }

            @Override
            public void onError(String message, int code) {
                if (view != null) {
                    view.onResponse(new ArrayList<>(), message);
                    view.hideLoader();
                }
            }

            @Override
            public void onFailed(String message) {
                if (view != null) {
                    view.onResponse(new ArrayList<>(), message);
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
        if (topScorerCall != null){
            if (topScorerCall.isExecuted()){
                topScorerCall.cancel();
                if (topScorerCall.isCanceled()){
                    view.hideLoader();
                    topScorerCall = null;
                }
            }
        }
        super.onDetach();
    }
}
