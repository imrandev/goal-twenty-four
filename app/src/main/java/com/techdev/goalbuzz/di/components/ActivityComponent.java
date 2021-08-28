package com.techdev.goalbuzz.di.components;

import com.techdev.goalbuzz.di.modules.ActivityModule;
import com.techdev.goalbuzz.di.scopes.ActivityScope;
import com.techdev.goalbuzz.ui.affiliate.AccessoryActivity;
import com.techdev.goalbuzz.ui.explore.ExploreActivity;
import com.techdev.goalbuzz.ui.fixture.FixtureActivity;
import com.techdev.goalbuzz.ui.info.InfoActivity;
import com.techdev.goalbuzz.ui.main.MainActivity;
import com.techdev.goalbuzz.ui.standing.StandingActivity;
import com.techdev.goalbuzz.ui.team.TeamActivity;
import com.techdev.goalbuzz.ui.top.TopActivity;
import com.techdev.goalbuzz.ui.videos.VideoHighlightsActivity;

import dagger.Component;

@ActivityScope
@Component(
        dependencies = AppComponent.class,
        modules = {
                ActivityModule.class
        }
)
public interface ActivityComponent {
    void inject(MainActivity mainActivity);
    void inject(FixtureActivity fixtureActivity);
    void inject(StandingActivity standingActivity);
    void inject(TopActivity topActivity);
    void inject(TeamActivity teamActivity);
    void inject(InfoActivity infoActivity);
    void inject(AccessoryActivity accessoryActivity);
    void inject(VideoHighlightsActivity videoHighlightsActivity);
    void inject(ExploreActivity exploreActivity);
}
