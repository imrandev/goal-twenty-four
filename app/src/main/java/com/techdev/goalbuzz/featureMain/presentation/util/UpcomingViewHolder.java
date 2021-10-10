package com.techdev.goalbuzz.featureMain.presentation.util;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.databinding.ViewDataBinding;

import com.techdev.goalbuzz.R;
import com.techdev.goalbuzz.core.adapter.BaseRecyclerClickListener;
import com.techdev.goalbuzz.core.adapter.BaseRecyclerViewHolder;
import com.techdev.goalbuzz.databinding.ItemEmptyViewBinding;
import com.techdev.goalbuzz.databinding.ItemUpcomingMatchBinding;
import com.techdev.goalbuzz.featureMain.domain.models.Match;
import com.techdev.goalbuzz.core.util.DateFormatter;

public class UpcomingViewHolder extends BaseRecyclerViewHolder<Match, BaseRecyclerClickListener<Match>> {

    private ItemUpcomingMatchBinding upcomingMatchBinding;
    private ItemEmptyViewBinding emptyMatchBinding;

    public UpcomingViewHolder(@NonNull ViewDataBinding binding, boolean _isEmpty) {
        super(binding);
        if (_isEmpty){
            emptyMatchBinding = (ItemEmptyViewBinding) binding;
        } else {
            this.upcomingMatchBinding = (ItemUpcomingMatchBinding) binding;
        }
    }

    @Override
    public void onBindView(Match match) {
        viewPopulateWithData(match, itemView.getContext());
    }

    @Override
    public void onEmptyView(String message) {
        emptyMatchBinding.tvErrorMessage.setText(message);
    }

    @Override
    public void onBindView(Match match, BaseRecyclerClickListener<Match> onItemClickedListener) {
        viewPopulateWithData(match, itemView.getContext());
        enableItemViewLongClick(match, onItemClickedListener);
        enableCustomViewItemClick(upcomingMatchBinding.btnNotification, match, onItemClickedListener);
    }

    @Override
    public void onBindView(Match object, int position, BaseRecyclerClickListener<Match> onItemClickedListener) {

    }

    private void viewPopulateWithData(Match match, Context context) {
        upcomingMatchBinding.btnNotification.setChecked(match.hasScheduled());
        upcomingMatchBinding.btnNotification.setText(match.hasScheduled() ? context.getString(R.string.cancel_notify) : context.getString(R.string.notify));
        upcomingMatchBinding.itemMatchDay.setText(String.format("Matchday %s", match.getMatchday()));
        upcomingMatchBinding.itemLeagueName.setText(String.format("%s", match.getCompetition().getName()));
        upcomingMatchBinding.itemMatchHomeTeam.setText(match.getHomeTeam().getName());
        upcomingMatchBinding.itemMatchAwayTeam.setText(match.getAwayTeam().getName());
        upcomingMatchBinding.itemMatchTime.setText(DateFormatter.getInstance().getTime(match.getUtcDate()));
    }
}
