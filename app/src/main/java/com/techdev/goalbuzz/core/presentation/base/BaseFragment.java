package com.techdev.goalbuzz.core.presentation.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;
import androidx.transition.Fade;
import androidx.transition.Transition;
import androidx.transition.TransitionManager;

import com.google.android.material.snackbar.Snackbar;
import com.techdev.goalbuzz.R;
import com.techdev.goalbuzz.app.GoalKick;
import com.techdev.goalbuzz.di.components.DaggerFragmentComponent;
import com.techdev.goalbuzz.di.components.FragmentComponent;
import com.techdev.goalbuzz.core.util.ResourceUtils;

import javax.inject.Inject;

public abstract class BaseFragment<P extends IBasePresenter> extends Fragment {

    @Inject
    public P presenter;
    protected Context context;
    protected Activity activity;

    @Inject
    public ResourceUtils resourceUtils;

    @LayoutRes
    protected abstract int getLayoutRes();
    protected void injectComponent(FragmentComponent fragmentComponent){}
    protected abstract void setViewBinding(ViewDataBinding viewBinding);

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
        activity = getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewDataBinding viewDataBinding
                = DataBindingUtil.inflate(inflater, getLayoutRes(), container, false);
        setViewBinding(viewDataBinding);
        View rootView = viewDataBinding.getRoot();
        FragmentComponent fragmentComponent = DaggerFragmentComponent
                .builder()
                .appComponent(((GoalKick) activity.getApplication()).getAppComponent())
                .build();
        injectComponent(fragmentComponent);
        return rootView;
    }

    protected Snackbar getSnack(View rootView){
        View baseRootView = rootView.findViewById(R.id.rootView);
        Snackbar snackbar = Snackbar.make(
                baseRootView, R.string.data_loading, Snackbar.LENGTH_INDEFINITE);
        ViewGroup contentLay = (ViewGroup) snackbar.getView()
                .findViewById(com.google.android.material.R.id.snackbar_text).getParent();
        ProgressBar item = new ProgressBar(context);
        contentLay.addView(item);
        return snackbar;
    }

    protected void toast(String message){
        Toast.makeText(context.getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    protected void show(Snackbar snackbar){
        if (!snackbar.isShown()){
            snackbar.show();
        }
    }

    protected void dismiss(Snackbar snackbar){
        if (snackbar.isShown()){
            snackbar.dismiss();
        }
    }

    @Override
    public void onDestroyView() {
        if (presenter != null) presenter.onDetach();
        super.onDestroyView();
    }

    protected void animate(View view, @IdRes int idRes) {
        View baseRootView = view.getRootView().findViewById(R.id.rootView);
        Transition transition = new Fade();
        transition.setDuration(600);
        transition.addTarget(idRes);
        TransitionManager.beginDelayedTransition((ViewGroup) baseRootView, transition);
        view.setVisibility(view.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
    }
}
