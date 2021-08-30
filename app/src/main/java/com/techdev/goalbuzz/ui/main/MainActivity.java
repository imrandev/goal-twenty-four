package com.techdev.goalbuzz.ui.main;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.material.snackbar.Snackbar;
import com.techdev.goalbuzz.R;
import com.techdev.goalbuzz.adapter.BaseRecyclerClickListener;
import com.techdev.goalbuzz.adapter.BaseRecyclerViewHolder;
import com.techdev.goalbuzz.adapter.RecyclerViewAdapter;
import com.techdev.goalbuzz.databinding.ActivityMainBinding;
import com.techdev.goalbuzz.di.components.ActivityComponent;
import com.techdev.goalbuzz.model.League;
import com.techdev.goalbuzz.model.Query;
import com.techdev.goalbuzz.model.live.Match;
import com.techdev.goalbuzz.ui.affiliate.AccessoryActivity;
import com.techdev.goalbuzz.ui.base.BaseActivity;
import com.techdev.goalbuzz.ui.explore.ExploreActivity;
import com.techdev.goalbuzz.ui.fixture.FixtureActivity;
import com.techdev.goalbuzz.ui.standing.StandingActivity;
import com.techdev.goalbuzz.ui.top.TopActivity;
import com.techdev.goalbuzz.util.Constant;
import com.techdev.goalbuzz.util.SmoothLinearLayoutManager;
import com.techdev.goalbuzz.viewholder.LeagueViewHolder;
import com.techdev.goalbuzz.viewholder.LiveViewHolder;
import com.techdev.goalbuzz.viewholder.UpcomingViewHolder;

import java.util.Collections;
import java.util.List;

public class MainActivity extends BaseActivity<MainContract.Presenter> implements MainContract.View {

    private MainContract.Presenter mMainPresenter;
    private ActivityMainBinding mainBinding;
    private Snackbar snackbar;
    private static final String TAG = "MainActivity";

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_main;
    }

    @Override
    protected int getMenuRes() {
        return R.menu.main_menu;
    }

    @Override
    protected void onConnectivityTask(boolean isConnected) {
        mainBinding.tvOfflineStatus.setVisibility(isConnected ? View.GONE : View.VISIBLE);
        mMainPresenter.onMatchApiTask();
        onAdLoaded();
    }

    @Override
    protected void injectComponent(ActivityComponent activityComponent) {
        activityComponent.inject(this);
    }

    @Override
    protected void initViewBinding(ViewDataBinding viewBinding) {
        this.mainBinding = (ActivityMainBinding) viewBinding;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mMainPresenter = presenter;
        mMainPresenter.onAttach(this);

        mMainPresenter.onLoadLeague();
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Today's Match");
        setSupportActionBar(toolbar);
        snackbar = getSnack();
        mainBinding.nestedScrollview.setSmoothScrollingEnabled(true);

        mainBinding.rvUpcomingList.setLayoutManager(new SmoothLinearLayoutManager(getApplicationContext(), RecyclerView.HORIZONTAL, false){
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        mainBinding.rvLiveList.setLayoutManager(new SmoothLinearLayoutManager(getApplicationContext(), RecyclerView.HORIZONTAL, false){
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        mainBinding.rvResultList.setLayoutManager(new SmoothLinearLayoutManager(getApplicationContext()){
            @Override
            public boolean canScrollHorizontally() {
                return false;
            }
        });

        mainBinding.rvResultList.setNestedScrollingEnabled(false);
        mainBinding.templateReviewBanner.btnReview.setOnClickListener(btnReviewClickListener);
        mainBinding.topMenu.templateAccessories.btnAccessory.setOnClickListener(btnAccessoryClickListener);
        mainBinding.topMenu.templateExplore.btnExplore.setOnClickListener(btnExploreClickListener);
    }

    private final View.OnClickListener btnReviewClickListener = v -> playStore();
    private final View.OnClickListener btnAccessoryClickListener = v -> navToAccessory();
    private final View.OnClickListener btnExploreClickListener = v -> navToExplore();

    public void navToExplore(){
        startActivity(new Intent(getApplicationContext(), ExploreActivity.class));
    }

    private void playStore() {
        Uri uri = Uri.parse("market://details?id=" + getApplicationContext().getPackageName());
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        // To count with Play market backstack, After pressing back button,
        // to taken back to our application, we need to add following flags to intent.
        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        try {
            startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + getApplicationContext().getPackageName())));
        }
    }

    private void onAdLoaded() {
        AdRequest adRequest = new AdRequest.Builder().build();
        mainBinding.adView.loadAd(adRequest);
        mainBinding.adViewTop.loadAd(adRequest);
        mainBinding.adView.setAdListener(adListener);
        mainBinding.adViewTop.setAdListener(adListener);
    }

    @Override
    public void showMessage(String message) {
        toast(message);
    }

    @Override
    public void showLoader() {
        snackbar.show();
    }

    @Override
    public void hideLoader() {
        snackbar.dismiss();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.action_point_table) {
            startActivity(new Intent(getApplicationContext(), StandingActivity.class));
        } else if (itemId == R.id.action_top_scorer) {
            startActivity(new Intent(getApplicationContext(), TopActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    private void showUpcomingList(List<Match> matchList, String message){
        RecyclerViewAdapter<Match, BaseRecyclerClickListener<Match>> mUpcomingMatchAdapter = new RecyclerViewAdapter<Match, BaseRecyclerClickListener<Match>>(matchList, upcomingMatchClickListener, getString(R.string.no_upcoming_match_available)) {
            @Override
            public BaseRecyclerViewHolder<Match, BaseRecyclerClickListener<Match>> getViewHolder(ViewGroup parent) {
                ViewDataBinding dataBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_upcoming_match, parent, false);
                return new UpcomingViewHolder(dataBinding, false);
            }

            @Override
            public BaseRecyclerViewHolder<Match, BaseRecyclerClickListener<Match>> getSingleHeaderViewHolder(ViewGroup parent) {
                return null;
            }

            @Override
            public BaseRecyclerViewHolder<Match, BaseRecyclerClickListener<Match>> getEmptyViewHolder(ViewGroup parent) {
                ViewDataBinding dataBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_empty_view, parent, false);
                return new UpcomingViewHolder(dataBinding, true);
            }
        };
        mainBinding.rvUpcomingList.setVisibility(matchList.size() > 0 ? View.VISIBLE : View.GONE);
        mainBinding.rvUpcomingList.setAdapter(mUpcomingMatchAdapter);
    }

    private void showLiveList(List<Match> matchList, String message){
        RecyclerViewAdapter<Match, BaseRecyclerClickListener<Match>> mLiveMatchAdapter = new RecyclerViewAdapter<Match, BaseRecyclerClickListener<Match>>(matchList, getString(R.string.no_live_match_available)) {
            @Override
            public BaseRecyclerViewHolder<Match, BaseRecyclerClickListener<Match>> getViewHolder(ViewGroup parent) {
                ViewDataBinding dataBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_live_match, parent, false);
                return new LiveViewHolder(dataBinding);
            }

            @Override
            public BaseRecyclerViewHolder<Match, BaseRecyclerClickListener<Match>> getSingleHeaderViewHolder(ViewGroup parent) {
                return null;
            }

            @Override
            public BaseRecyclerViewHolder<Match, BaseRecyclerClickListener<Match>> getEmptyViewHolder(ViewGroup parent) {
                ViewDataBinding dataBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_empty_view, parent, false);
                return new LiveViewHolder(dataBinding);
            }
        };
        mainBinding.rvLiveList.setVisibility(matchList.size() > 0 ? View.VISIBLE : View.GONE);
        mainBinding.rvLiveList.setAdapter(mLiveMatchAdapter);
    }

    private void showResultsList(List<Match> matchList, String message){
        RecyclerViewAdapter<Match, BaseRecyclerClickListener<Match>> mResultsMatchAdapter = new RecyclerViewAdapter<Match, BaseRecyclerClickListener<Match>>(matchList, "No Match Result Available!") {
            @Override
            public BaseRecyclerViewHolder<Match, BaseRecyclerClickListener<Match>> getViewHolder(ViewGroup parent) {
                ViewDataBinding dataBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_live_match, parent, false);
                return new LiveViewHolder(dataBinding);
            }

            @Override
            public BaseRecyclerViewHolder<Match, BaseRecyclerClickListener<Match>> getSingleHeaderViewHolder(ViewGroup parent) {
                return null;
            }

            @Override
            public BaseRecyclerViewHolder<Match, BaseRecyclerClickListener<Match>> getEmptyViewHolder(ViewGroup parent) {
                ViewDataBinding dataBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_empty_view, parent, false);
                return new LiveViewHolder(dataBinding);
            }
        };
        mainBinding.rvResultList.setVisibility(matchList.size() > 0 ? View.VISIBLE : View.GONE);
        mainBinding.rvResultList.setAdapter(mResultsMatchAdapter);
    }

    private void navToFixturesWthExtras(String extras){
        startActivity(new Intent(getApplicationContext(), FixtureActivity.class).putExtra(Constant.LEAGUE_ID, extras));
    }

    private void navToAccessory(){
        startActivity(new Intent(getApplicationContext(), AccessoryActivity.class));
    }

    @Override
    protected void onDestroy() {
        mainBinding.adView.destroy();
        mainBinding.adViewTop.destroy();
        clearMemory();
        mainBinding = null;
        super.onDestroy();
    }

    @Override
    public void onLiveView(List<Match> matchList, String message, int count) {
        Collections.shuffle(matchList);
        runOnUiThread(() -> {
            mainBinding.tvLiveCaption.setVisibility(View.VISIBLE);
            mainBinding.tvLiveCaption.setText(String.format("Live Match (%s)", count));
            showLiveList(matchList, message);
        });
    }

    @Override
    public void onUpcomingView(List<Match> matchList, String message, int count) {
        Collections.shuffle(matchList);
        runOnUiThread(() -> {
            mainBinding.tvUpcomingCaption.setText(String.format("Upcoming Match (%s)", count));
            showUpcomingList(matchList, message);
        });
    }

    @Override
    public void onFinishedView(List<Match> matchList, String message, int count) {
        Collections.shuffle(matchList);
        runOnUiThread(() -> {
            mainBinding.tvResultCaption.setVisibility(View.VISIBLE);
            mainBinding.tvResultCaption.setText(String.format("Match Result (%s)", count));
            showResultsList(matchList, message);
        });
    }

    @Override
    public void onTeamView(List<Query> teamList, String message, int count) {

    }

    @Override
    public void onLeagueView(List<League> leagueList) {
        RecyclerViewAdapter<League, BaseRecyclerClickListener<League>> leagueRecyclerViewAdapter = new RecyclerViewAdapter<League, BaseRecyclerClickListener<League>>(leagueList, leagueRecyclerClickListener) {
            @Override
            public BaseRecyclerViewHolder<League, BaseRecyclerClickListener<League>> getViewHolder(ViewGroup parent) {
                ViewDataBinding dataBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_league, parent, false);
                return new LeagueViewHolder(dataBinding, getApplicationContext());
            }

            @Override
            public BaseRecyclerViewHolder<League, BaseRecyclerClickListener<League>> getSingleHeaderViewHolder(ViewGroup parent) {
                return null;
            }

            @Override
            public BaseRecyclerViewHolder<League, BaseRecyclerClickListener<League>> getEmptyViewHolder(ViewGroup parent) {
                ViewDataBinding dataBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_empty_view, parent, false);
                return new LeagueViewHolder(dataBinding, getApplicationContext());
            }
        };
        mainBinding.rvLeagueView.setAdapter(leagueRecyclerViewAdapter);
    }

    @Override
    public void onChangeMarquee(String text) {
        runOnUiThread(() -> {
            mainBinding.tvMarquee.setVisibility(View.VISIBLE);
            mainBinding.tvMarquee.setSelected(true);
            mainBinding.tvMarquee.setText(text);
        });
    }

    private final BaseRecyclerClickListener<League> leagueRecyclerClickListener = (view, item, position) -> {
        navToFixturesWthExtras(resourceUtils.getLeagueIds()[position]);
    };

    private final AdListener adListener = new AdListener(){
        @Override
        public void onAdLoaded() {
            super.onAdLoaded();
            mainBinding.tvAdViewTop.setVisibility(View.VISIBLE);
            mainBinding.adView.setVisibility(View.VISIBLE);
            mainBinding.adViewTop.setVisibility(View.VISIBLE);
        }

        @Override
        public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
            super.onAdFailedToLoad(loadAdError);
            mainBinding.tvAdViewTop.setVisibility(View.GONE);
            mainBinding.adView.setVisibility(View.GONE);
            mainBinding.adViewTop.setVisibility(View.GONE);
        }
    };

    @Override
    public void onPause() {
        if (mainBinding != null) {
            mainBinding.adView.pause();
            mainBinding.adViewTop.pause();
        }
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mainBinding != null) {
            mainBinding.adView.resume();
            mainBinding.adViewTop.resume();
        }
    }

    private void clearMemory() {
        mainBinding.rvLeagueView.setAdapter(null);
        mainBinding.rvLiveList.setAdapter(null);
        mainBinding.rvResultList.setAdapter(null);
        mainBinding.rvUpcomingList.setAdapter(null);
    }


    private final BaseRecyclerClickListener<Match> upcomingMatchClickListener = new BaseRecyclerClickListener<Match>() {
        @Override
        public void onItemClickListener(View view, Match item, int position) {
            CheckBox checkBox = view.findViewById(R.id.btn_notification);
            mMainPresenter.scheduleMatch(item, checkBox);
        }
    };

    @Override
    public void updateCheckBox(CheckBox checkBox, boolean scheduled) {
        checkBox.setChecked(scheduled);
        checkBox.setText(scheduled ? R.string.cancel_notify : R.string.notify);
    }
}
