package com.techdev.goalbuzz.viewholder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.techdev.goalbuzz.R;
import com.techdev.goalbuzz.core.adapter.BaseRecyclerClickListener;
import com.techdev.goalbuzz.core.adapter.BaseRecyclerViewHolder;
import com.techdev.goalbuzz.core.adapter.RecyclerViewAdapter;
import com.techdev.goalbuzz.databinding.ItemEmptyViewBinding;
import com.techdev.goalbuzz.databinding.ItemPointTableMultiBinding;
import com.techdev.goalbuzz.di.scopes.ApplicationContext;
import com.techdev.goalbuzz.model.point.Standing;
import com.techdev.goalbuzz.model.point.Table;

import java.util.List;

import javax.inject.Inject;

public class MultiHeaderPointHolder extends BaseRecyclerViewHolder<Standing, BaseRecyclerClickListener<Standing>> {

    private ItemPointTableMultiBinding multiBinding;
    private ItemEmptyViewBinding itemEpmtyViewBinding;

    @Inject
    @ApplicationContext
    Context context;

    public MultiHeaderPointHolder(@NonNull ViewDataBinding binding) {
        super(binding);
        if (binding instanceof ItemPointTableMultiBinding){
            this.multiBinding = (ItemPointTableMultiBinding) binding;
            multiBinding.rvClPointList.setLayoutManager(new LinearLayoutManager(context));
            multiBinding.rvClPointList.setNestedScrollingEnabled(false);
        } else if (binding instanceof ItemEmptyViewBinding){
            this.itemEpmtyViewBinding = (ItemEmptyViewBinding) binding;
        }
    }

    @Override
    public void onBindView(Standing standing) {
        initViewWithData(standing);
    }

    @Override
    public void onEmptyView(String message) {
        if (itemEpmtyViewBinding != null){
            itemEpmtyViewBinding.tvErrorMessage.setText(message);
        }
    }

    @Override
    public void onBindView(Standing standing, BaseRecyclerClickListener<Standing> onItemClickedListener) {
        initViewWithData(standing);
    }

    @Override
    public void onBindView(Standing object, int position, BaseRecyclerClickListener<Standing> onItemClickedListener) {

    }

    private void initViewWithData(Standing standing){
        multiBinding.itemTvGroupName.setText(String.format("%s : %s",
                standing.getGroup().toString().replace("_", " "),  standing.getType()));
        List<Table> tableList = standing.getTable();
        RecyclerViewAdapter<Table, BaseRecyclerClickListener<Table>> tableAdapter = new RecyclerViewAdapter<Table, BaseRecyclerClickListener<Table>>(tableList, "Point table not available!") {
            @Override
            public BaseRecyclerViewHolder<Table, BaseRecyclerClickListener<Table>> getViewHolder(ViewGroup parent) {
                ViewDataBinding dataBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                        R.layout.item_point_table, parent, false);
                return new SingleHeaderPointHolder(dataBinding);
            }

            @Override
            public BaseRecyclerViewHolder<Table, BaseRecyclerClickListener<Table>> getSingleHeaderViewHolder(ViewGroup parent) {
                return null;
            }

            @Override
            public BaseRecyclerViewHolder<Table, BaseRecyclerClickListener<Table>> getEmptyViewHolder(ViewGroup parent) {
                ViewDataBinding dataBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                        R.layout.item_empty_view, parent, false);
                return new SingleHeaderPointHolder(dataBinding);
            }
        };
        multiBinding.rvClPointList.setAdapter(tableAdapter);
    }
}
