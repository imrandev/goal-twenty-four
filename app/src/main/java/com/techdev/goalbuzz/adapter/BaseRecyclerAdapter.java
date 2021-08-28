package com.techdev.goalbuzz.adapter;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public abstract class BaseRecyclerAdapter<T, L> extends RecyclerView.Adapter<BaseRecyclerViewHolder<T,L>> {

    public abstract BaseRecyclerViewHolder<T,L> getEmptyViewHolder(ViewGroup parent);

    @NonNull
    @Override
    public BaseRecyclerViewHolder<T, L> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseRecyclerViewHolder<T, L> holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
