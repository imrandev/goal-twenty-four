package com.techdev.goalbuzz.ui.top;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.ViewDataBinding;
import androidx.navigation.Navigation;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.techdev.goalbuzz.R;
import com.techdev.goalbuzz.databinding.ActivityTopBinding;
import com.techdev.goalbuzz.di.components.ActivityComponent;
import com.techdev.goalbuzz.interfaces.PassExtraToActivity;
import com.techdev.goalbuzz.core.presentation.base.BaseActivity;
import com.techdev.goalbuzz.core.util.Constant;

import java.util.Objects;

public class TopActivity extends BaseActivity<TopContract.Presenter> implements View.OnClickListener, PassExtraToActivity {

    private static final String TAG = "TopActivity";
    private ActivityTopBinding topBinding;
    private byte _btnStateEPL = 0, _btnStateLaLiga = -1, _btnStateUEFA = -1, _btnStateSerieA = -1, _btnStateBundesliga = -1, _btnStateLigue = -1;

    private InterstitialAd mInterstitialAd;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_top;
    }

    @Override
    protected int getMenuRes() {
        return 0;
    }

    @Override
    protected void onConnectivityTask(boolean isConnected) {
        topBinding.tvOfflineStatus.setVisibility(isConnected ? View.GONE : View.VISIBLE);
    }

    @Override
    protected void injectComponent(ActivityComponent activityComponent) {
        activityComponent.inject(this);
    }

    @Override
    protected void initViewBinding(ViewDataBinding viewBinding) {
        topBinding = (ActivityTopBinding) viewBinding;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Top Goal Scorer");
        setSupportActionBar(toolbar);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        topBinding.topCardView.btnEpl.setBackground(AppCompatResources.getDrawable(getApplicationContext(), R.drawable.white_circle_background));
        topBinding.topCardView.btnLaliga.setBackground(AppCompatResources.getDrawable(getApplicationContext(), R.drawable.white_circle_background));
        topBinding.topCardView.btnSeriea.setBackground(AppCompatResources.getDrawable(getApplicationContext(), R.drawable.white_circle_background));
        topBinding.topCardView.btnUefa.setBackground(AppCompatResources.getDrawable(getApplicationContext(), R.drawable.white_circle_background));
        topBinding.topCardView.btnBundesliga.setBackground(AppCompatResources.getDrawable(getApplicationContext(), R.drawable.white_circle_background));
        topBinding.topCardView.btnLigue.setBackground(AppCompatResources.getDrawable(getApplicationContext(), R.drawable.white_circle_background));

        topBinding.topCardView.btnEpl.setOnClickListener(this);
        topBinding.topCardView.btnLaliga.setOnClickListener(this);
        topBinding.topCardView.btnSeriea.setOnClickListener(this);
        topBinding.topCardView.btnUefa.setOnClickListener(this);
        topBinding.topCardView.btnBundesliga.setOnClickListener(this);
        topBinding.topCardView.btnLigue.setOnClickListener(this);

        topBinding.tvCaption.setText(getString(R.string.english_premier_league));
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
                topBinding.tvCaption.setText(getString(R.string.english_premier_league));
            }
        } else if (id == R.id.btn_laliga) {
            if (_btnStateLaLiga == -1) {
                fragmentTransaction("PD");
                _btnStateEPL = -1;
                _btnStateLaLiga = 0;
                _btnStateUEFA = -1;
                _btnStateSerieA = -1;
                topBinding.tvCaption.setText(getString(R.string.spanish_primera_division));
            }
        } else if (id == R.id.btn_uefa) {
            if (_btnStateUEFA == -1) {
                fragmentTransaction("CL");
                _btnStateEPL = -1;
                _btnStateLaLiga = -1;
                _btnStateUEFA = 0;
                _btnStateSerieA = -1;
                topBinding.tvCaption.setText(getString(R.string.champions_league));
            }
        } else if (id == R.id.btn_seriea) {
            if (_btnStateSerieA == -1) {
                fragmentTransaction("SA");
                _btnStateEPL = -1;
                _btnStateLaLiga = -1;
                _btnStateUEFA = -1;
                _btnStateSerieA = 0;
                topBinding.tvCaption.setText(getString(R.string.italian_serie));
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
                topBinding.tvCaption.setText(getString(R.string.bundesliga));
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
                topBinding.tvCaption.setText(getString(R.string.ligue));
            }
        }
    }

    private void fragmentTransaction(String leagueId) {
        Bundle extras = new Bundle();
        extras.putString(Constant.LEAGUE_ID, leagueId);
        Navigation.findNavController(this,
                R.id.nav_host_fragment).navigate(R.id.action_topFragment_to_topFragment, extras);
    }

    @Override
    protected void onDestroy() {
        topBinding = null;
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
            interstitialAd.show(TopActivity.this);
        }

        @Override
        public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
            mInterstitialAd = null;
            Log.i(TAG, loadAdError.getMessage());
        }
    };
}
