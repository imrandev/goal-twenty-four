package com.techdev.goalbuzz.core.adapter;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

import com.techdev.goalbuzz.core.adapter.event.OnLongClickListener;
import com.techdev.goalbuzz.core.adapter.event.OnSingleClickListener;

import static com.techdev.goalbuzz.core.util.Constant.CLICK_TIME_INTERVAL;

public abstract class BaseRecyclerViewHolder<T, L> extends RecyclerView.ViewHolder {

    private long mLastClickTime = System.currentTimeMillis();

    public BaseRecyclerViewHolder(@NonNull ViewDataBinding binding) {
        super(binding.getRoot());
    }

    public abstract void onBindView(T object);
    public abstract void onEmptyView(String message);
    public abstract void onBindView(T object, L onItemClickedListener);
    public abstract void onBindView(T object, int position,  L onItemClickedListener);

    protected void enableItemViewClick(T item, BaseRecyclerClickListener<T> baseRecyclerClickListener){

        itemView.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                long now = System.currentTimeMillis();
                if (now - mLastClickTime < CLICK_TIME_INTERVAL) {
                    return;
                }
                mLastClickTime = now;
                if (baseRecyclerClickListener != null){
                    baseRecyclerClickListener.onItemClickListener(itemView, item, getBindingAdapterPosition());
                }
            }
        });
    }

    protected void enableItemViewLongClick(T item, BaseRecyclerClickListener<T> baseRecyclerClickListener){
        itemView.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public void onLongPressed(View v) {
                long now = System.currentTimeMillis();
                if (now - mLastClickTime < CLICK_TIME_INTERVAL) {
                    return;
                }
                mLastClickTime = now;
                if (baseRecyclerClickListener != null){
                    baseRecyclerClickListener.onItemClickListener(itemView, item, getAdapterPosition());
                }
            }
        });
    }

    protected void enableCustomViewItemClick(View view, T item, BaseRecyclerClickListener<T> baseRecyclerClickListener){
        view.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                if (baseRecyclerClickListener != null){
                    long now = System.currentTimeMillis();
                    if (now - mLastClickTime < CLICK_TIME_INTERVAL) {
                        return;
                    }
                    mLastClickTime = now;
                    baseRecyclerClickListener.onItemClickListener(itemView, item, getAdapterPosition());
                }
            }
        });
    }
}
