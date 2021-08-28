package com.techdev.goalbuzz.viewholder;

import androidx.annotation.NonNull;
import androidx.databinding.ViewDataBinding;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.techdev.goalbuzz.adapter.BaseRecyclerClickListener;
import com.techdev.goalbuzz.adapter.BaseRecyclerViewHolder;
import com.techdev.goalbuzz.databinding.ItemEmptyViewBinding;
import com.techdev.goalbuzz.databinding.ItemPointTableBinding;
import com.techdev.goalbuzz.model.point.Table;

public class SingleHeaderPointHolder extends BaseRecyclerViewHolder<Table, BaseRecyclerClickListener<Table>> {

    private ItemPointTableBinding tableBinding;
    private ItemEmptyViewBinding emptyBinding;
    private final RequestManager mRequestManager;

    public SingleHeaderPointHolder(@NonNull ViewDataBinding binding) {
        super(binding);
        if (binding instanceof ItemPointTableBinding){
            this.tableBinding = (ItemPointTableBinding) binding;
        } else if (binding instanceof ItemEmptyViewBinding){
            this.emptyBinding = (ItemEmptyViewBinding) binding;
        }
        mRequestManager = Glide.with(binding.getRoot());
    }

    @Override
    public void onBindView(Table table) {
        initViewWithData(table);
    }

    @Override
    public void onEmptyView(String message) {
        if (emptyBinding != null){
            emptyBinding.tvErrorMessage.setText(message);
        }
    }

    @Override
    public void onBindView(Table table, BaseRecyclerClickListener<Table> onItemClickedListener) {
        initViewWithData(table);
    }

    @Override
    public void onBindView(Table object, int position, BaseRecyclerClickListener<Table> onItemClickedListener) {

    }

    private void initViewWithData(Table table){
        mRequestManager.load(table.getTeam().getCrestUrl())
                .apply(new RequestOptions().override(
                        tableBinding.itemIvTeamLogo.getMeasuredWidth(),
                        tableBinding.itemIvTeamLogo.getMeasuredHeight()))
                .into(tableBinding.itemIvTeamLogo);

        tableBinding.itemTvTeamPosition.setText(String.format("%s", table.getPosition()));
        tableBinding.itemTvTeamName.setText(String.format("%s", table.getTeam().getName()));
        tableBinding.itemTvMatchPlayed.setText(String.format("%s", table.getPlayedGames()));
        tableBinding.itemTvMatchWon.setText(String.format("%s", table.getWon()));
        tableBinding.itemTvMatchDraw.setText(String.format("%s", table.getDraw()));
        tableBinding.itemTvMatchLost.setText(String.format("%s", table.getLost()));
        tableBinding.itemTvGoalFor.setText(String.format("%s", table.getGoalsFor()));
        tableBinding.itemTvGoalAgainst.setText(String.format("%s", table.getGoalsAgainst()));
        tableBinding.itemTvGoalDifference.setText(String.format("%s", table.getGoalDifference()));
        tableBinding.itemTvMatchPoints.setText(String.format("%s", table.getPoints()));
    }
}
