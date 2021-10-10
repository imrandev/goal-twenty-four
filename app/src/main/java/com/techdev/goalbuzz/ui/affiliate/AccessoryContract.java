package com.techdev.goalbuzz.ui.affiliate;

import com.techdev.goalbuzz.model.market.Product;
import com.techdev.goalbuzz.core.presentation.base.IBasePresenter;
import com.techdev.goalbuzz.core.presentation.base.IBaseView;

import java.util.List;

public class AccessoryContract {
    public interface View extends IBaseView {
        void onResponse(List<Product> matchList, String message);
    }

    public interface Presenter extends IBasePresenter<View> {
        void executeAccessoryTask();
    }
}
