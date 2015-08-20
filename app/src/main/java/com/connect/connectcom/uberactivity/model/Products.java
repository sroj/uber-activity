package com.connect.connectcom.uberactivity.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Products {

    @SerializedName("products") private List<Product> products;
    private boolean ok = true;

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public boolean isOk() {
        return ok;
    }

    @Override
    public String toString() {
        return "Products{" +
               "products=" + products +
               '}';
    }

    public static Products buildErrorObject() {
        Products products = new Products();
        products.ok = false;
        return products;
    }
}
