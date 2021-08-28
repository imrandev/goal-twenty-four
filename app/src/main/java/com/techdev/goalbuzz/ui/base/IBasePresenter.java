package com.techdev.goalbuzz.ui.base;

public interface IBasePresenter<T> {
    void onAttach(T view);
    void onDetach();
    boolean isConnected();
    void adViewUpdate(int count, String activityName);
    int getAdCount(String activityName);
    boolean showAd(String activityName);
}
