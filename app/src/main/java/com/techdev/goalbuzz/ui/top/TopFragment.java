package com.techdev.goalbuzz.ui.top;


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
import com.techdev.goalbuzz.adapter.BaseRecyclerClickListener;
import com.techdev.goalbuzz.adapter.BaseRecyclerViewHolder;
import com.techdev.goalbuzz.adapter.RecyclerViewAdapter;
import com.techdev.goalbuzz.databinding.FragmentTopBinding;
import com.techdev.goalbuzz.di.components.FragmentComponent;
import com.techdev.goalbuzz.interfaces.PassExtraToActivity;
import com.techdev.goalbuzz.model.League;
import com.techdev.goalbuzz.model.top.Scorer;
import com.techdev.goalbuzz.ui.base.BaseFragment;
import com.techdev.goalbuzz.util.Constant;
import com.techdev.goalbuzz.viewholder.TopViewHolder;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class TopFragment extends BaseFragment<TopContract.Presenter> implements TopContract.View {

    private String leagueId = "PL";
    private Snackbar snackbar;
    private FragmentTopBinding topBinding;
    private TopContract.Presenter mTopPresenter;

    private PassExtraToActivity extraToActivity;

    public TopFragment() {
        // Required empty public constructor
    }

    @Override
    protected void injectComponent(FragmentComponent fragmentComponent) {
        fragmentComponent.inject(this);
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_top;
    }

    @Override
    protected void setViewBinding(ViewDataBinding viewBinding) {
        topBinding = (FragmentTopBinding) viewBinding;
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
        snackbar = getSnack(view);

        extraToActivity = (PassExtraToActivity) activity;

        mTopPresenter = presenter;
        mTopPresenter.onAttach(this);

        topBinding.rvTopScorerList.setLayoutManager(new LinearLayoutManager(context));
        mTopPresenter.onTopScorerApiTask(leagueId);
    }

    @Override
    public void onResponse(List<Scorer> scorerList, String message) {
        RecyclerViewAdapter<Scorer, BaseRecyclerClickListener<Scorer>> topAdapter = new RecyclerViewAdapter<Scorer, BaseRecyclerClickListener<Scorer>>(scorerList, "Top Scorer\nNot Available") {
            @Override
            public BaseRecyclerViewHolder<Scorer, BaseRecyclerClickListener<Scorer>> getViewHolder(ViewGroup parent) {
                ViewDataBinding dataBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                        R.layout.item_top_scorer, parent, false);
                return new TopViewHolder(dataBinding);
            }

            @Override
            public BaseRecyclerViewHolder<Scorer, BaseRecyclerClickListener<Scorer>> getSingleHeaderViewHolder(ViewGroup parent) {
                return null;
            }

            @Override
            public BaseRecyclerViewHolder<Scorer, BaseRecyclerClickListener<Scorer>> getEmptyViewHolder(ViewGroup parent) {
                ViewDataBinding dataBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                        R.layout.item_empty_view, parent, false);
                return new TopViewHolder(dataBinding);
            }
        };
        topBinding.rvTopScorerList.setAdapter(topAdapter);
        extraToActivity.onPassed(new Bundle());
    }

    @Override
    public void onLeagueView(List<League> leagueList) {

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
        mTopPresenter.onDetach();
        topBinding = null;
        snackbar.dismiss();
        snackbar = null;
        super.onDestroyView();
    }
}
