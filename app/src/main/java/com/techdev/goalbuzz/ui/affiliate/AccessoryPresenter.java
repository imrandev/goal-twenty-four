package com.techdev.goalbuzz.ui.affiliate;

import com.techdev.goalbuzz.model.market.Amazon;
import com.techdev.goalbuzz.model.market.Product;
import com.techdev.goalbuzz.network.response.EnqueueResponse;
import com.techdev.goalbuzz.ui.base.BasePresenter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import retrofit2.Call;

public class AccessoryPresenter extends BasePresenter<AccessoryContract.View> implements AccessoryContract.Presenter {

    private Call<Amazon> amazonCall;

    @Inject
    AccessoryPresenter() {

    }

    @Override
    public void executeAccessoryTask() {
        view.showLoader();
        amazonCall = amazonClient.getRepository().findAmazonProducts();
        amazonCall.enqueue(new EnqueueResponse<Amazon>() {
            @Override
            public void onReceived(Amazon body, String message) {
                List<Product> products = body.getProducts();
                if (products != null && !products.isEmpty()){
                    updateView(products, message);
                } else {
                    updateView(new ArrayList<>(2), message);
                }
            }

            @Override
            public void onError(String message, int code) {
                updateView(new ArrayList<>(2), message);
            }

            @Override
            public void onFailed(String message) {
                updateView(new ArrayList<>(2), message);
            }
        });
    }

    private void updateView(List<Product> products, String message){
        if (view != null) {
            view.onResponse(products, message);
            view.hideLoader();
        }
    }

    @Override
    public void onDetach() {
        if (amazonCall != null){
            if (amazonCall.isExecuted()){
                amazonCall.cancel();
                if (amazonCall.isCanceled()){
                    view.hideLoader();
                    amazonCall = null;
                }
            }
        }
        super.onDetach();
    }
}
