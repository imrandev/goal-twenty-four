package com.techdev.goalbuzz.ui.info;

import com.techdev.goalbuzz.core.presentation.base.IBasePresenter;
import com.techdev.goalbuzz.core.presentation.base.IBaseView;

/**
 * Created by Imran Khan on 3/12/2021.
 * Email : context.imran@gmail.com
 */
public class InfoContract {

    public interface View extends IBaseView {

    }

    public interface Presenter extends IBasePresenter<InfoContract.View> {

    }
}
