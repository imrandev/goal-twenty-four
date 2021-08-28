package com.techdev.goalbuzz.viewholder;

import androidx.annotation.NonNull;
import androidx.databinding.ViewDataBinding;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.techdev.goalbuzz.R;
import com.techdev.goalbuzz.adapter.BaseRecyclerClickListener;
import com.techdev.goalbuzz.adapter.BaseRecyclerViewHolder;
import com.techdev.goalbuzz.databinding.ItemAccessoryBinding;
import com.techdev.goalbuzz.databinding.ItemEmptyViewBinding;
import com.techdev.goalbuzz.model.market.Product;

public class AccessoryViewHolder extends BaseRecyclerViewHolder<Product, BaseRecyclerClickListener<Product>> {

    private ItemAccessoryBinding accessoryBinding;
    private ItemEmptyViewBinding emptyBinding;
    private final RequestManager mRequestManager;

    public AccessoryViewHolder(@NonNull ViewDataBinding binding) {
        super(binding);
        if (binding instanceof ItemAccessoryBinding){
            this.accessoryBinding = (ItemAccessoryBinding) binding;
        } else if (binding instanceof ItemEmptyViewBinding){
            this.emptyBinding = (ItemEmptyViewBinding) binding;
        }
        mRequestManager = Glide.with(binding.getRoot());
    }

    @Override
    public void onBindView(Product object) {

    }

    @Override
    public void onEmptyView(String message) {
        if (emptyBinding != null){
            emptyBinding.tvErrorMessage.setText(message);
        }
    }

    @Override
    public void onBindView(Product product, BaseRecyclerClickListener<Product> onItemClickedListener) {
        mRequestManager.load(product.getImgUrl())
                .apply(new RequestOptions().override(
                        accessoryBinding.itemProductImage.getMeasuredWidth(),
                        accessoryBinding.itemProductImage.getMeasuredHeight()))
                .error(R.drawable.ic_product)
                .placeholder(R.drawable.ic_product)
                .into(accessoryBinding.itemProductImage);
        accessoryBinding.itemTvProductName.setText(product.getName());
        enableCustomViewItemClick(accessoryBinding.btnBuyNow, product, onItemClickedListener);
    }

    @Override
    public void onBindView(Product object, int position, BaseRecyclerClickListener<Product> onItemClickedListener) {

    }
}
