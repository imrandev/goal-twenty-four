package com.techdev.goalbuzz.viewholder;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.databinding.ViewDataBinding;

import com.techdev.goalbuzz.core.adapter.BaseRecyclerClickListener;
import com.techdev.goalbuzz.core.adapter.BaseRecyclerViewHolder;
import com.techdev.goalbuzz.databinding.ItemEmptyViewBinding;
import com.techdev.goalbuzz.databinding.ItemFixtureBinding;
import com.techdev.goalbuzz.model.fixture.Match;
import com.techdev.goalbuzz.core.util.MatchUtil;

public class FixtureViewHolder extends BaseRecyclerViewHolder<Match, BaseRecyclerClickListener<Match>> {

    private ItemFixtureBinding fixtureBinding;
    private ItemEmptyViewBinding emptyMatchBinding;

    public FixtureViewHolder(@NonNull ViewDataBinding binding, Context context) {
        super(binding);
        if (binding instanceof ItemEmptyViewBinding){
            emptyMatchBinding = (ItemEmptyViewBinding) binding;
        } else if (binding instanceof ItemFixtureBinding){
            this.fixtureBinding = (ItemFixtureBinding) binding;
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
        fixtureBinding.itemMatchDay.setText(String.format("Matchday %s", match.getMatchday()));
        fixtureBinding.itemMatchHomeTeam.setText(match.getHomeTeam().getName());
        fixtureBinding.itemMatchAwayTeam.setText(match.getAwayTeam().getName());
        fixtureBinding.itemMatchTime.setText(String.format("%s", MatchUtil.getInstance().getPlayTime(match)));
    }
}
