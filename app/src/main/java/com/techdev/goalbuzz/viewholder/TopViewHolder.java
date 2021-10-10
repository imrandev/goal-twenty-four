package com.techdev.goalbuzz.viewholder;

import androidx.annotation.NonNull;
import androidx.databinding.ViewDataBinding;

import com.techdev.goalbuzz.core.adapter.BaseRecyclerClickListener;
import com.techdev.goalbuzz.core.adapter.BaseRecyclerViewHolder;
import com.techdev.goalbuzz.databinding.ItemEmptyViewBinding;
import com.techdev.goalbuzz.databinding.ItemTopScorerBinding;
import com.techdev.goalbuzz.model.top.Scorer;
import com.techdev.goalbuzz.core.util.DateFormatter;

public class TopViewHolder extends BaseRecyclerViewHolder<Scorer, BaseRecyclerClickListener<Scorer>> {

    private ItemTopScorerBinding topScorerBinding;
    private ItemEmptyViewBinding emptyBinding;

    public TopViewHolder(@NonNull ViewDataBinding binding) {
        super(binding);

        if (binding instanceof ItemTopScorerBinding){
            this.topScorerBinding = (ItemTopScorerBinding) binding;
        } else {
            this.emptyBinding = (ItemEmptyViewBinding) binding;
        }
    }

    @Override
    public void onBindView(Scorer scorer) {
        initViewsWithData(scorer);
    }

    @Override
    public void onEmptyView(String message) {
        if (emptyBinding != null){
            emptyBinding.tvErrorMessage.setText(message);
        }
    }

    @Override
    public void onBindView(Scorer scorer, BaseRecyclerClickListener<Scorer> onItemClickedListener) {
        initViewsWithData(scorer);
    }

    @Override
    public void onBindView(Scorer object, int position, BaseRecyclerClickListener<Scorer> onItemClickedListener) {

    }

    private void initViewsWithData(Scorer scorer){
        topScorerBinding.itemJerseyNo.setText(String.format("%s", scorer.getPlayer().getShirtNumber()));
        topScorerBinding.itemPlayerBirthday.setText(String.format("Birthday : %s", DateFormatter.getInstance().getDay(scorer.getPlayer().getDateOfBirth())));
        topScorerBinding.itemPlayerName.setText(String.format("%s", scorer.getPlayer().getName()));
        topScorerBinding.itemPlayerPosition.setText(String.format("Position : %s", scorer.getPlayer().getPosition()));
        topScorerBinding.itemPlayerCountry.setText(String.format("Nationality : %s", scorer.getPlayer().getNationality()));
        topScorerBinding.itemTvScore.setText(String.format("%s", scorer.getNumberOfGoals()));
        topScorerBinding.itemTvTeamName.setText(String.format("%s", scorer.getTeam().getName()));
    }
}
