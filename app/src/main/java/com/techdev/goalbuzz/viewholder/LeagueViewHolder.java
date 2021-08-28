package com.techdev.goalbuzz.viewholder;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.databinding.ViewDataBinding;

import com.techdev.goalbuzz.R;
import com.techdev.goalbuzz.adapter.BaseRecyclerClickListener;
import com.techdev.goalbuzz.adapter.BaseRecyclerViewHolder;
import com.techdev.goalbuzz.databinding.ItemEmptyViewBinding;
import com.techdev.goalbuzz.databinding.ItemLeagueBinding;
import com.techdev.goalbuzz.model.League;

public class LeagueViewHolder extends BaseRecyclerViewHolder<League, BaseRecyclerClickListener<League>> {

    private ItemEmptyViewBinding emptyMatchBinding;
    private ItemLeagueBinding itemLeagueBinding;

    private final Context context;

    public LeagueViewHolder(@NonNull ViewDataBinding binding, Context context) {
        super(binding);
        this.context = context;
        if (binding instanceof ItemEmptyViewBinding){
            emptyMatchBinding = (ItemEmptyViewBinding) binding;
        } else if (binding instanceof ItemLeagueBinding){
            this.itemLeagueBinding = (ItemLeagueBinding) binding;
        }
    }

    @Override
    public void onBindView(League object) {

    }

    @Override
    public void onEmptyView(String message) {
        if (emptyMatchBinding != null){
            emptyMatchBinding.tvErrorMessage.setText(message);
        }
    }

    @Override
    public void onBindView(League object, BaseRecyclerClickListener<League> onItemClickedListener) {

    }

    @Override
    public void onBindView(League object, int position, BaseRecyclerClickListener<League> onItemClickedListener) {
        itemLeagueBinding.itemIvIcon.setImageResource(object.getDrawableRes());
        itemLeagueBinding.itemTvName.setText(object.getName());
        onBackgroundChanged(position % 2 == 1);
        enableItemViewClick(object, onItemClickedListener);
    }

    private void onBackgroundChanged(boolean isOdd){
        itemLeagueBinding.itemView.setBackground(context.getDrawable(
                isOdd ? R.drawable.yellow_rounded_background : R.drawable.green_rounded_background));
    }
}
