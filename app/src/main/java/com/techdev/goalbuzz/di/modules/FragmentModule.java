package com.techdev.goalbuzz.di.modules;

import com.techdev.goalbuzz.di.scopes.FragmentScope;
import com.techdev.goalbuzz.ui.standing.StandingContract;
import com.techdev.goalbuzz.ui.standing.StandingPresenter;
import com.techdev.goalbuzz.ui.top.TopContract;
import com.techdev.goalbuzz.ui.top.TopPresenter;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class FragmentModule {

    @Binds
    @FragmentScope
    abstract StandingContract.Presenter bindStandingPresenter(StandingPresenter standingPresenter);

    @Binds
    @FragmentScope
    abstract TopContract.Presenter bindTopPresenter(TopPresenter topPresenter);
}
