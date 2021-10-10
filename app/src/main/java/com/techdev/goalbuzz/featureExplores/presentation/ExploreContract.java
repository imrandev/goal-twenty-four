package com.techdev.goalbuzz.featureExplores.presentation;

import com.techdev.goalbuzz.core.presentation.base.IBasePresenter;
import com.techdev.goalbuzz.core.presentation.base.IBaseView;

public class ExploreContract {

    public interface View extends IBaseView {

    }

    public interface Presenter extends IBasePresenter<ExploreContract.View> {

    }
}
