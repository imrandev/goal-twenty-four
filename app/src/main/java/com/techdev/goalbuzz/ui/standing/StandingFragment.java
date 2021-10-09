package com.techdev.goalbuzz.ui.standing;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.snackbar.Snackbar;
import com.techdev.goalbuzz.R;
import com.techdev.goalbuzz.core.adapter.BaseRecyclerClickListener;
import com.techdev.goalbuzz.core.adapter.BaseRecyclerViewHolder;
import com.techdev.goalbuzz.core.adapter.RecyclerViewAdapter;
import com.techdev.goalbuzz.databinding.FragmentStandingBinding;
import com.techdev.goalbuzz.di.components.FragmentComponent;
import com.techdev.goalbuzz.interfaces.PassExtraToActivity;
import com.techdev.goalbuzz.model.League;
import com.techdev.goalbuzz.model.point.Standing;
import com.techdev.goalbuzz.model.point.Table;
import com.techdev.goalbuzz.ui.base.BaseFragment;
import com.techdev.goalbuzz.util.Constant;
import com.techdev.goalbuzz.viewholder.MultiHeaderPointHolder;
import com.techdev.goalbuzz.viewholder.SingleHeaderPointHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class StandingFragment extends BaseFragment<StandingContract.Presenter> implements StandingContract.View {

    private String leagueId = "PL";
    private Snackbar snackbar;
    private FragmentStandingBinding standingBinding;
    private StandingContract.Presenter mStandingPresenter;

    private PassExtraToActivity extraToActivity;

    public StandingFragment() {
        //Required empty public constructor
    }

    @Override
    protected void injectComponent(FragmentComponent fragmentComponent) {
        fragmentComponent.inject(this);
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_standing;
    }

    @Override
    protected void setViewBinding(ViewDataBinding viewBinding) {
        standingBinding = (FragmentStandingBinding) viewBinding;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getArguments();
        if (extras != null){
            leagueId = extras.getString(Constant.LEAGUE_ID);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        extraToActivity = (PassExtraToActivity) activity;

        snackbar = getSnack(view);
        mStandingPresenter = presenter;
        mStandingPresenter.onAttach(this);

        standingBinding.rvStandings.setLayoutManager(new LinearLayoutManager(context));
        standingBinding.rvStandings.setNestedScrollingEnabled(false);
        mStandingPresenter.onStandingApiTask(leagueId);
    }

    @Override
    public void showMessage(String message) {
        toast(message);
    }

    @Override
    public void showLoader() {
        show(snackbar);
    }

    @Override
    public void hideLoader() {
        dismiss(snackbar);
    }

    @Override
    public void onDestroyView() {
        mStandingPresenter.onDetach();
        standingBinding = null;
        snackbar.dismiss();
        snackbar = null;
        super.onDestroyView();
    }

    @Override
    public void onResponse(List<Standing> standingList, String message) {
        if (!leagueId.equals("CL") && !leagueId.equals("EC")){
            List<Table> tableList = standingList.isEmpty() ? new ArrayList<>(2) : standingList.get(0).getTable();
            RecyclerViewAdapter<Table, BaseRecyclerClickListener<Table>> pointAdapter = new RecyclerViewAdapter<Table, BaseRecyclerClickListener<Table>>(tableList, "Point table\nnot available") {
                @Override
                public BaseRecyclerViewHolder<Table, BaseRecyclerClickListener<Table>> getViewHolder(ViewGroup parent) {
                    ViewDataBinding dataBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                            R.layout.item_point_table, parent, false);
                    return new SingleHeaderPointHolder(dataBinding);
                }

                @Override
                public BaseRecyclerViewHolder<Table, BaseRecyclerClickListener<Table>> getSingleHeaderViewHolder(ViewGroup parent) {
                    ViewDataBinding dataBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                            R.layout.item_point_table_header, parent, false);
                    return new SingleHeaderPointHolder(dataBinding);
                }

                @Override
                public BaseRecyclerViewHolder<Table, BaseRecyclerClickListener<Table>> getEmptyViewHolder(ViewGroup parent) {
                    ViewDataBinding dataBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                            R.layout.item_empty_view, parent, false);
                    return new SingleHeaderPointHolder(dataBinding);
                }
            };
            pointAdapter.setHeaderSingleTime(true);
            standingBinding.rvStandings.setAdapter(pointAdapter);
        } else {
            RecyclerViewAdapter<Standing, BaseRecyclerClickListener<Standing>> standingAdapter = new RecyclerViewAdapter<Standing, BaseRecyclerClickListener<Standing>>(standingList, "Point table\nnot found!") {
                @Override
                public BaseRecyclerViewHolder<Standing, BaseRecyclerClickListener<Standing>> getViewHolder(ViewGroup parent) {
                    ViewDataBinding dataBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                            R.layout.item_point_table_multi, parent, false);
                    return new MultiHeaderPointHolder(dataBinding);
                }

                @Override
                public BaseRecyclerViewHolder<Standing, BaseRecyclerClickListener<Standing>> getSingleHeaderViewHolder(ViewGroup parent) {
                    return null;
                }

                @Override
                public BaseRecyclerViewHolder<Standing, BaseRecyclerClickListener<Standing>> getEmptyViewHolder(ViewGroup parent) {
                    ViewDataBinding dataBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                            R.layout.item_empty_view, parent, false);
                    return new MultiHeaderPointHolder(dataBinding);
                }
            };
            standingBinding.rvStandings.setAdapter(standingAdapter);
        }
        extraToActivity.onPassed(new Bundle());
    }

    @Override
    public void onLeagueView(List<League> leagueList) {

    }
}
