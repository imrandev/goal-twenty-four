package com.techdev.goalbuzz.ui.team;

import android.os.Bundle;
import android.view.View;

import androidx.databinding.ViewDataBinding;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.snackbar.Snackbar;
import com.techdev.goalbuzz.R;
import com.techdev.goalbuzz.databinding.ActivityTeamBinding;
import com.techdev.goalbuzz.di.components.ActivityComponent;
import com.techdev.goalbuzz.model.team.Team;
import com.techdev.goalbuzz.ui.base.BaseActivity;
import com.techdev.goalbuzz.util.Constant;

public class TeamActivity extends BaseActivity<TeamContract.Presenter> implements TeamContract.View {

    private Snackbar snackbar;
    private ActivityTeamBinding teamBinding;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_team;
    }

    @Override
    protected int getMenuRes() {
        return 0;
    }

    @Override
    protected void injectComponent(ActivityComponent activityComponent) {
        activityComponent.inject(this);
    }

    @Override
    protected void initViewBinding(ViewDataBinding viewBinding) {
        teamBinding = (ActivityTeamBinding) viewBinding;
    }

    @Override
    protected void onConnectivityTask(boolean isConnected) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String team = getIntent().getStringExtra(Constant.TEAM);

        TeamContract.Presenter mTeamPresenter = presenter;
        mTeamPresenter.onAttach(this);
        snackbar = getSnack();

        if (team != null){
            String [] teamArray = team.split("\\s+");
            if (teamArray.length >= 2){
                mTeamPresenter.onTeamApiTask(String.format("%s %s", teamArray[0], teamArray[1]));
            } else {
                mTeamPresenter.onTeamApiTask(team);
            }
        } else {
            onResponse(null, "Team Not Found");
        }
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
    public void onResponse(Team team, String message) {
        if (team != null){
            setDataIntoViews(team);
        } else {
            updateUi(false);
        }
    }

    private void setDataIntoViews(Team team){

        updateUi(true);

        Glide.with(this)
                .load(team.getStrTeamBadge())
                .apply(new RequestOptions()
                        .override(teamBinding.ivTeamBadge.getMeasuredWidth(),
                                teamBinding.ivTeamBadge.getMeasuredHeight()))
                .into(teamBinding.ivTeamBadge);

        Glide.with(this)
                .load(team.getStrStadiumThumb())
                .apply(new RequestOptions()
                        .override(teamBinding.ivStadium.getMeasuredWidth(),
                                teamBinding.ivStadium.getMeasuredHeight()))
                .into(teamBinding.ivStadium);

        teamBinding.tvTeamName.setText(team.getStrTeam());
        teamBinding.tvLeagueName.setText(team.getStrLeague());
        teamBinding.tvFormedYear.setText(String.format("Formed %s", team.getIntFormedYear()));
        teamBinding.tvDesc.setText(team.getStrDescriptionEN());
        teamBinding.tvStadium.setText(String.format("%s, %s", team.getStrStadium(), team.getStrStadiumLocation()));
        teamBinding.tvStadiumDesc.setText(team.getStrStadiumDescription());
    }

    private void updateUi(boolean isVisible){
        teamBinding.tvErrorMessage.setVisibility(isVisible ? View.GONE : View.VISIBLE);
        teamBinding.toolbarLayout.setVisibility(isVisible ? View.VISIBLE : View.GONE);
        teamBinding.clTeamTopView.setVisibility(isVisible ? View.VISIBLE : View.GONE);
        teamBinding.clTeamInfoBoard.setVisibility(isVisible ? View.VISIBLE : View.GONE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        snackbar.dismiss();
        snackbar = null;
        teamBinding = null;
    }
}
