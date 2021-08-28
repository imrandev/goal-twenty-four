package com.techdev.goalbuzz.viewholder;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.databinding.ViewDataBinding;

import com.techdev.goalbuzz.R;
import com.techdev.goalbuzz.adapter.BaseRecyclerClickListener;
import com.techdev.goalbuzz.adapter.BaseRecyclerViewHolder;
import com.techdev.goalbuzz.databinding.ItemEmptyViewBinding;
import com.techdev.goalbuzz.databinding.ItemTeamBinding;
import com.techdev.goalbuzz.model.Query;

public class NameViewHolder extends BaseRecyclerViewHolder<Query, BaseRecyclerClickListener<Query>> {

    private final Context context;
    private ItemEmptyViewBinding emptyViewBinding;
    private ItemTeamBinding teamBinding;

    public NameViewHolder(@NonNull ViewDataBinding binding, Context context) {
        super(binding);

        if (binding instanceof ItemTeamBinding){
            this.teamBinding = (ItemTeamBinding) binding;
        } else if (binding instanceof ItemEmptyViewBinding){
            this.emptyViewBinding = (ItemEmptyViewBinding) binding;
        }
        this.context = context;
    }

    @Override
    public void onBindView(Query query) {
        initViewWithData(query);
    }

    @Override
    public void onEmptyView(String message) {
        if (emptyViewBinding != null){
            emptyViewBinding.tvErrorMessage.setText(message);
        }
    }

    @Override
    public void onBindView(Query query, BaseRecyclerClickListener<Query> onItemClickedListener) {
        initViewWithData(query);
        enableItemViewClick(query, onItemClickedListener);
    }

    @Override
    public void onBindView(Query object, int position, BaseRecyclerClickListener<Query> onItemClickedListener) {

    }

    private void initViewWithData(Query query){
        teamBinding.itemTvTeamName.setBackground(AppCompatResources
                .getDrawable(context, R.drawable.yellow_rounded_background));
        teamBinding.itemTvTeamName.setText(query.getName());
    }
}
