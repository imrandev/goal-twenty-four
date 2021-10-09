package com.techdev.goalbuzz.core.adapter;

import android.view.View;

public interface BaseRecyclerClickListener<T> {
    void onItemClickListener(View view, T item, int position);
}
