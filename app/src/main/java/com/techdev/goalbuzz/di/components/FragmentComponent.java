package com.techdev.goalbuzz.di.components;

import com.techdev.goalbuzz.di.modules.FragmentModule;
import com.techdev.goalbuzz.di.scopes.FragmentScope;
import com.techdev.goalbuzz.ui.standing.StandingFragment;
import com.techdev.goalbuzz.ui.top.TopFragment;

import dagger.Component;

@FragmentScope
@Component(
        dependencies = AppComponent.class,
        modules = {
                FragmentModule.class
        }
)
public interface FragmentComponent {
    void inject(StandingFragment standingFragment);
    void inject(TopFragment topFragment);
}
