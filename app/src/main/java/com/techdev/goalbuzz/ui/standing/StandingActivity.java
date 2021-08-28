package com.techdev.goalbuzz.ui.standing;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.ViewDataBinding;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.techdev.goalbuzz.R;
import com.techdev.goalbuzz.databinding.ActivityStandingBinding;
import com.techdev.goalbuzz.di.components.ActivityComponent;
import com.techdev.goalbuzz.interfaces.PassExtraToActivity;
import com.techdev.goalbuzz.ui.base.BaseActivity;
import com.techdev.goalbuzz.ui.fixture.FixtureActivity;
import com.techdev.goalbuzz.util.Constant;

import java.util.Objects;

public class StandingActivity extends BaseActivity<StandingContract.Presenter> implements View.OnClickListener, PassExtraToActivity {

    private static final String TAG = "StandingActivity";

    private ActivityStandingBinding standingBinding;
    private byte _btnStateEPL = 0, _btnStateLaLiga = -1, _btnStateUEFA = -1, _btnStateSerieA = -1, _btnStateBundesliga = -1, _btnStateLigue = -1;

    private InterstitialAd mInterstitialAd;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_standing;
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
        standingBinding = (ActivityStandingBinding) viewBinding;
    }

    @Override
    protected void onConnectivityTask(boolean isConnected) {
        standingBinding.tvOfflineStatus.setVisibility(isConnected ? View.GONE : View.VISIBLE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String leagueId = getIntent().getStringExtra(Constant.LEAGUE_ID);
        String name = getIntent().getStringExtra(Constant.LEAGUE_NAME);
        standingBinding.topCardView.rootView.setVisibility(leagueId != null ? View.GONE : View.VISIBLE);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Standings");
        setSupportActionBar(toolbar);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        if (leagueId != null){
            standingBinding.tvCaption.setText(name);
            fragmentTransaction(leagueId);
        } else {
            standingBinding.topCardView.btnEpl.setBackground(AppCompatResources.getDrawable(getApplicationContext(), R.drawable.white_circle_background));
            standingBinding.topCardView.btnLaliga.setBackground(AppCompatResources.getDrawable(getApplicationContext(), R.drawable.white_circle_background));
            standingBinding.topCardView.btnSeriea.setBackground(AppCompatResources.getDrawable(getApplicationContext(), R.drawable.white_circle_background));
            standingBinding.topCardView.btnUefa.setBackground(AppCompatResources.getDrawable(getApplicationContext(), R.drawable.white_circle_background));
            standingBinding.topCardView.btnBundesliga.setBackground(AppCompatResources.getDrawable(getApplicationContext(), R.drawable.white_circle_background));
            standingBinding.topCardView.btnLigue.setBackground(AppCompatResources.getDrawable(getApplicationContext(), R.drawable.white_circle_background));

            standingBinding.topCardView.btnEpl.setOnClickListener(this);
            standingBinding.topCardView.btnLaliga.setOnClickListener(this);
            standingBinding.topCardView.btnSeriea.setOnClickListener(this);
            standingBinding.topCardView.btnUefa.setOnClickListener(this);
            standingBinding.topCardView.btnBundesliga.setOnClickListener(this);
            standingBinding.topCardView.btnLigue.setOnClickListener(this);

            standingBinding.tvCaption.setText(getString(R.string.english_premier_league));
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.btn_epl) {
            if (_btnStateEPL == -1) {
                fragmentTransaction("PL");
                _btnStateEPL = 0;
                _btnStateLaLiga = -1;
                _btnStateUEFA = -1;
                _btnStateSerieA = -1;
                standingBinding.tvCaption.setText(getString(R.string.english_premier_league));
            }
        } else if (id == R.id.btn_laliga) {
            if (_btnStateLaLiga == -1) {
                fragmentTransaction("PD");
                _btnStateEPL = -1;
                _btnStateLaLiga = 0;
                _btnStateUEFA = -1;
                _btnStateSerieA = -1;
                standingBinding.tvCaption.setText(getString(R.string.spanish_primera_division));
            }
        } else if (id == R.id.btn_uefa) {
            if (_btnStateUEFA == -1) {
                fragmentTransaction("CL");
                _btnStateEPL = -1;
                _btnStateLaLiga = -1;
                _btnStateUEFA = 0;
                _btnStateSerieA = -1;
                standingBinding.tvCaption.setText(getString(R.string.champions_league));
            }
        } else if (id == R.id.btn_seriea) {
            if (_btnStateSerieA == -1) {
                fragmentTransaction("SA");
                _btnStateEPL = -1;
                _btnStateLaLiga = -1;
                _btnStateUEFA = -1;
                _btnStateSerieA = 0;
                standingBinding.tvCaption.setText(getString(R.string.italian_serie));
            }
        } else if (id == R.id.btn_bundesliga) {
            if (_btnStateBundesliga == -1) {
                fragmentTransaction("BL1");
                _btnStateEPL = -1;
                _btnStateLaLiga = -1;
                _btnStateUEFA = -1;
                _btnStateSerieA = -1;
                _btnStateBundesliga = 0;
                _btnStateLigue = -1;
                standingBinding.tvCaption.setText(getString(R.string.bundesliga));
            }
        } else if (id == R.id.btn_ligue) {
            if (_btnStateLigue == -1) {
                fragmentTransaction("FL1");
                _btnStateEPL = -1;
                _btnStateLaLiga = -1;
                _btnStateUEFA = -1;
                _btnStateSerieA = -1;
                _btnStateLigue = 0;
                _btnStateBundesliga = -1;
                standingBinding.tvCaption.setText(getString(R.string.ligue));
            }
        }
    }

    private void fragmentTransaction(String leagueId) {
        Bundle extras = new Bundle();
        extras.putString(Constant.LEAGUE_ID, leagueId);
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        if (navHostFragment != null) {
            NavController navController = navHostFragment.getNavController();
            navController.navigate(R.id.action_standingFragment_to_standingFragment, extras);
        }
    }

    @Override
    protected void onDestroy() {
        standingBinding = null;
        mInterstitialAd = null;
        super.onDestroy();
    }

    @Override
    public void onPassed(Bundle bundle) {
        if (mInterstitialAd == null){
            onViewAd();
        }
    }

    private void onViewAd(){
        InterstitialAd.load(getApplicationContext(),
                getString(R.string.interstitial_ads), new AdRequest.Builder().build(), adLoadCallback);
    }

    private final InterstitialAdLoadCallback adLoadCallback = new InterstitialAdLoadCallback() {
        @Override
        public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
            mInterstitialAd = interstitialAd;
            interstitialAd.show(StandingActivity.this);
        }

        @Override
        public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
            mInterstitialAd = null;
            Log.i(TAG, loadAdError.getMessage());
        }
    };
}
