package com.techdev.goalbuzz.ui.fixture;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.material.snackbar.Snackbar;
import com.techdev.goalbuzz.R;
import com.techdev.goalbuzz.core.adapter.BaseRecyclerClickListener;
import com.techdev.goalbuzz.core.adapter.BaseRecyclerViewHolder;
import com.techdev.goalbuzz.core.adapter.RecyclerViewAdapter;
import com.techdev.goalbuzz.databinding.ActivityFixtureBinding;
import com.techdev.goalbuzz.di.components.ActivityComponent;
import com.techdev.goalbuzz.model.League;
import com.techdev.goalbuzz.model.fixture.Match;
import com.techdev.goalbuzz.ui.base.BaseActivity;
import com.techdev.goalbuzz.util.Constant;
import com.techdev.goalbuzz.viewholder.FixtureViewHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FixtureActivity extends BaseActivity<FixtureContract.Presenter> implements FixtureContract.View, SearchView.OnQueryTextListener, BaseRecyclerClickListener<Match> {

    private static final String TAG = "FixtureActivity";

    private ActivityFixtureBinding fixtureBinding;
    private FixtureContract.Presenter mFixturePresenter;
    private Snackbar snackbar;

    private SearchView searchView;
    private SearchView.SearchAutoComplete searchAutoComplete;

    private RecyclerViewAdapter<Match, BaseRecyclerClickListener<Match>> fixtureAdapter;
    private List<Match> initList;

    private String leagueId;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_fixture;
    }

    @Override
    protected int getMenuRes() {
        return R.menu.fixture_menu;
    }

    @Override
    protected void onConnectivityTask(boolean isConnected) {
        fixtureBinding.tvOfflineStatus.setVisibility(isConnected ? View.GONE : View.VISIBLE);
        mFixturePresenter.onFixtureApiTask(leagueId);
    }

    @Override
    protected void injectComponent(ActivityComponent activityComponent) {
        activityComponent.inject(this);
    }

    @Override
    protected void initViewBinding(ViewDataBinding viewBinding) {
        fixtureBinding = (ActivityFixtureBinding) viewBinding;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        leagueId = getIntent().getStringExtra(Constant.LEAGUE_ID);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Fixtures");
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        snackbar = getSnack();
        initList = new ArrayList<>(2);

        fixtureBinding.rvFixtureList.setLayoutManager(new LinearLayoutManager(this));
        fixtureBinding.rvFixtureList.setHasFixedSize(true);

        mFixturePresenter = presenter;
        mFixturePresenter.onAttach(this);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
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
    public void onResponse(List<Match> matchList, String message) {
        this.initList = matchList;
        fixtureAdapter = new RecyclerViewAdapter<Match,
                BaseRecyclerClickListener<Match>>(matchList, this,  message) {
            @Override
            public BaseRecyclerViewHolder<Match, BaseRecyclerClickListener<Match>> getViewHolder(ViewGroup parent) {
                ViewDataBinding dataBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                        R.layout.item_fixture, parent, false);
                return new FixtureViewHolder(dataBinding, getApplicationContext());
            }

            @Override
            public BaseRecyclerViewHolder<Match, BaseRecyclerClickListener<Match>> getSingleHeaderViewHolder(ViewGroup parent) {
                return null;
            }

            @Override
            public BaseRecyclerViewHolder<Match, BaseRecyclerClickListener<Match>> getEmptyViewHolder(ViewGroup parent) {
                ViewDataBinding dataBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                        R.layout.item_empty_view, parent, false);
                return new FixtureViewHolder(dataBinding, getApplicationContext());
            }
        };
        fixtureBinding.rvFixtureList.setAdapter(fixtureAdapter);
        // add team suggestions in autocomplete
        presenter.addTeamSuggestions(matchList);
    }

    private void onViewAd(){
        InterstitialAd.load(getApplicationContext(),
                getString(R.string.interstitial_ads), new AdRequest.Builder().build(), adLoadCallback);
    }

    private final InterstitialAdLoadCallback adLoadCallback = new InterstitialAdLoadCallback() {
        @Override
        public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
            interstitialAd.show(FixtureActivity.this);
        }

        @Override
        public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
            Log.i(TAG, loadAdError.getMessage());
        }
    };

    @Override
    public void onLeagueView(List<League> leagueList) {

    }

    @Override
    public void loadAutocomplete(List<String> teams) {
        runOnUiThread(() -> {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(),
                    android.R.layout.simple_list_item_1, teams);
            if (searchAutoComplete == null) initAutoCompleteSearch();
            searchAutoComplete.setAdapter(adapter);
            //show interstitial ad
            onViewAd();
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.fixture_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.searchBar);
        searchView = (SearchView) searchItem.getActionView();
        searchView.setQueryHint("Search by team");
        initAutoCompleteSearch();
        return true;
    }

    void initAutoCompleteSearch(){
        searchAutoComplete = searchView.findViewById(androidx.appcompat.R.id.search_src_text);
        searchView.setOnQueryTextListener(this);
        searchAutoComplete.setOnItemClickListener(onItemClickListener);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        filterMatchByTeam(newText);
        return true;
    }

    private final AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            String name = (String) parent.getItemAtPosition(position);
            searchAutoComplete.setText(name);
            filterMatchByTeam(name);
        }
    };

    private void filterMatchByTeam(String name) {
        if (name.isEmpty()) fixtureAdapter.update(initList);
        if (name.contains("//s")){
            name = name.replace(" ", "");
        }
        String finalName = name;
        new Handler().post(() -> {
            List<Match> filteredList = new ArrayList<>(2);
            for (Match match : initList){
                String team = String.format("%s%s", match.getHomeTeam().getName(), match.getAwayTeam().getName());
                if (team.toLowerCase().contains(finalName.toLowerCase())){
                    filteredList.add(match);
                }
            }
            runOnUiThread(() -> fixtureAdapter.update(filteredList));
        });
    }

    @Override
    public void onBackPressed() {
        if (!searchView.isIconified()){
            searchView.setIconified(true);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onItemClickListener(View view, Match item, int position) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        snackbar.dismiss();
        snackbar = null;
        fixtureBinding = null;
    }
}
