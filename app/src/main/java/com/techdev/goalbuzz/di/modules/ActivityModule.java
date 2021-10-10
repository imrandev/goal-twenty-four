package com.techdev.goalbuzz.di.modules;

import com.techdev.goalbuzz.di.scopes.ActivityScope;
import com.techdev.goalbuzz.ui.affiliate.AccessoryContract;
import com.techdev.goalbuzz.ui.affiliate.AccessoryPresenter;
import com.techdev.goalbuzz.featureExplores.presentation.ExploreContract;
import com.techdev.goalbuzz.featureExplores.presentation.ExplorePresenter;
import com.techdev.goalbuzz.ui.fixture.FixtureContract;
import com.techdev.goalbuzz.ui.fixture.FixturePresenter;
import com.techdev.goalbuzz.ui.info.InfoContract;
import com.techdev.goalbuzz.ui.info.InfoPresenter;
import com.techdev.goalbuzz.featureMain.presentation.MainContract;
import com.techdev.goalbuzz.featureMain.presentation.MainPresenter;
import com.techdev.goalbuzz.ui.standing.StandingContract;
import com.techdev.goalbuzz.ui.standing.StandingPresenter;
import com.techdev.goalbuzz.ui.team.TeamContract;
import com.techdev.goalbuzz.ui.team.TeamPresenter;
import com.techdev.goalbuzz.ui.top.TopContract;
import com.techdev.goalbuzz.ui.top.TopPresenter;
import com.techdev.goalbuzz.ui.videos.VideoHighlightsContract;
import com.techdev.goalbuzz.ui.videos.VideoHighlightsPresenter;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class ActivityModule {

    @Binds
    @ActivityScope
    abstract MainContract.Presenter bindMainPresenter(MainPresenter mainPresenter);

    @Binds
    @ActivityScope
    abstract FixtureContract.Presenter bindFixturePresenter(FixturePresenter fixturePresenter);

    @Binds
    @ActivityScope
    abstract StandingContract.Presenter bindStandingPresenter(StandingPresenter standingPresenter);

    @Binds
    @ActivityScope
    abstract TopContract.Presenter bindTopPresenter(TopPresenter topPresenter);

    @Binds
    @ActivityScope
    abstract TeamContract.Presenter bindTeamPresenter(TeamPresenter topPresenter);

    @Binds
    @ActivityScope
    abstract InfoContract.Presenter bindInfoPresenter(InfoPresenter infoPresenter);

    @Binds
    @ActivityScope
    abstract AccessoryContract.Presenter bindAccessoryPresenter(AccessoryPresenter accessoryPresenter);

    @Binds
    @ActivityScope
    abstract VideoHighlightsContract.Presenter bindVideoHighlightsPresenter(VideoHighlightsPresenter videoHighlightsPresenter);

    @Binds
    @ActivityScope
    abstract ExploreContract.Presenter bindExplorePresenter(ExplorePresenter explorePresenter);
}
