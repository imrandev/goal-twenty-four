package com.techdev.goalbuzz.core.adapter;

import android.view.ViewGroup;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public abstract class RecyclerViewAdapter<T, L> extends BaseRecyclerAdapter<T,L> {

    private List<T> itemList;
    private L onItemClickedListener;
    private final int EMPTY_VIEW = 0;
    private final int SINGLE_HEADER_VIEW = 2;
    private String message;

    private boolean _singleHeaderView;

    protected RecyclerViewAdapter(String message) {
        this.itemList = new ArrayList<>();
        this.message = message;
    }

    protected RecyclerViewAdapter(List<T> itemList) {
        if (itemList == null) {
            this.itemList = new ArrayList<>();
        } else {
            this.itemList = itemList;
        }
    }

    protected RecyclerViewAdapter(List<T> itemList, String message) {
        if (itemList == null) {
            this.itemList = new ArrayList<>();
        } else {
            this.itemList = itemList;
        }
        this.message = message;
    }

    protected RecyclerViewAdapter(List<T> itemList, L onItemClickedListener) {
        if (itemList == null) {
            this.itemList = new ArrayList<>();
        } else {
            this.itemList = itemList;
            this.onItemClickedListener = onItemClickedListener;
        }
    }

    protected RecyclerViewAdapter(List<T> itemList, L onItemClickedListener, String message) {
        if (itemList == null) {
            this.itemList = new ArrayList<>();
        } else {
            this.itemList = itemList;
            this.onItemClickedListener = onItemClickedListener;
        }
        this.message = message;
    }

    public void update(List<T> filterList){
        this.itemList =  filterList;
        notifyDataSetChanged();
    }

    public void setHeaderSingleTime(boolean isSingle){
        this._singleHeaderView = isSingle;
    }

    public abstract BaseRecyclerViewHolder<T,L> getViewHolder(ViewGroup parent);
    public abstract BaseRecyclerViewHolder<T,L> getSingleHeaderViewHolder(ViewGroup parent);

    @NonNull
    @Override
    public BaseRecyclerViewHolder<T,L> onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if (i == SINGLE_HEADER_VIEW){
            return getSingleHeaderViewHolder(viewGroup);
        }
        return itemList.isEmpty() ? getEmptyViewHolder(viewGroup) : getViewHolder(viewGroup);
    }

    @Override
    public void onBindViewHolder(@NonNull BaseRecyclerViewHolder<T,L> holder, int position) {
        if (getItemViewType(position) != EMPTY_VIEW && getItemViewType(position) != SINGLE_HEADER_VIEW) {
            if (onItemClickedListener != null){
                if (_singleHeaderView){
                    holder.onBindView(itemList.get(position - 1), onItemClickedListener);
                } else {
                    holder.onBindView(itemList.get(position), onItemClickedListener);
                    holder.onBindView(itemList.get(position), position,  onItemClickedListener);
                }
            } else {
                if (_singleHeaderView){
                    holder.onBindView(itemList.get(position - 1));
                } else {
                    holder.onBindView(itemList.get(position));
                }
            }
        } else if (getItemViewType(position) == EMPTY_VIEW){
            holder.onEmptyView(message);
        }
    }

    @Override
    public int getItemCount() {
        int s = itemList.size();
        if (_singleHeaderView){
            s = s + 1;
        }
        return (itemList.isEmpty()) ? 1 : s ;
    }

    @Override
    public int getItemViewType(int position) {
        return (itemList.isEmpty()) ? EMPTY_VIEW
                : _singleHeaderView ? position == 0 ? SINGLE_HEADER_VIEW : 1 : 1;
    }

    public List<T> getItemList(){
        return itemList != null ? itemList : new ArrayList<>(2);
    }
}
