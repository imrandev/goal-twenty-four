package com.techdev.goalbuzz.model.market;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Amazon {
    @SerializedName("products")
    @Expose
    private List<Product> products = null;
    @SerializedName("total")
    @Expose
    private Integer total;

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }
}
