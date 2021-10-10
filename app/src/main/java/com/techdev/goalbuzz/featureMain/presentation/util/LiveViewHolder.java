package com.techdev.goalbuzz.featureMain.presentation.util;

import androidx.annotation.NonNull;
import androidx.databinding.ViewDataBinding;

import com.techdev.goalbuzz.core.adapter.BaseRecyclerClickListener;
import com.techdev.goalbuzz.core.adapter.BaseRecyclerViewHolder;
import com.techdev.goalbuzz.databinding.ItemEmptyViewBinding;
import com.techdev.goalbuzz.databinding.ItemLiveMatchBinding;
import com.techdev.goalbuzz.featureMain.domain.models.Match;
import com.techdev.goalbuzz.featureMain.domain.models.Score;

public class LiveViewHolder extends BaseRecyclerViewHolder<Match, BaseRecyclerClickListener<Match>> {

    private ItemLiveMatchBinding liveMatchBinding;
    private ItemEmptyViewBinding emptyMatchBinding;

    public LiveViewHolder(@NonNull ViewDataBinding binding) {
        super(binding);
        if (binding instanceof  ItemEmptyViewBinding){
            this.emptyMatchBinding = (ItemEmptyViewBinding) binding;
        } else if (binding instanceof ItemLiveMatchBinding){
            this.liveMatchBinding = (ItemLiveMatchBinding) binding;
        }
    }

    @Override
    public void onBindView(Match match) {
        viewPopulateWithData(match);
    }

    @Override
    public void onEmptyView(String message) {
        if (emptyMatchBinding != null){
            emptyMatchBinding.tvErrorMessage.setText(message);
        }
    }

    @Override
    public void onBindView(Match match, BaseRecyclerClickListener<Match> onItemClickedListener) {
        viewPopulateWithData(match);
    }

    @Override
    public void onBindView(Match object, int position, BaseRecyclerClickListener<Match> onItemClickedListener) {

    }

    private void viewPopulateWithData(Match match) {
        liveMatchBinding.itemMatchDay.setText(String.format("Matchday %s", match.getMatchday()));
        liveMatchBinding.itemLeagueName.setText(String.format("%s", match.getCompetition().getName()));
        liveMatchBinding.itemMatchHomeTeam.setText(match.getHomeTeam().getName());
        liveMatchBinding.itemMatchAwayTeam.setText(match.getAwayTeam().getName());
        liveMatchBinding.itemMatchStatus.setText(match.getStatus().replace("_", " "));

        Score score = match.getScore();
        if (score != null){
            int home = score.getPenalties().getHomeTeam() != null ?
                    score.getPenalties().getHomeTeam() : score.getExtraTime().getHomeTeam() != null ?
                    score.getExtraTime().getHomeTeam() : score.getFullTime().getHomeTeam() != null ?
                    score.getFullTime().getHomeTeam() : score.getHalfTime().getHomeTeam() != null ?
                    score.getHalfTime().getHomeTeam() : 0;

            int away = score.getPenalties().getAwayTeam() != null ?
                    score.getPenalties().getAwayTeam() : score.getExtraTime().getAwayTeam() != null ?
                    score.getExtraTime().getAwayTeam() : score.getFullTime().getAwayTeam() != null ?
                    score.getFullTime().getAwayTeam() : score.getHalfTime().getAwayTeam() != null ?
                    score.getHalfTime().getAwayTeam() : 0;

            liveMatchBinding.itemMatchHomeScore.setText(String.format("%s", home));
            liveMatchBinding.itemMatchAwayScore.setText(String.format("%s", away));
        }
    }
}
