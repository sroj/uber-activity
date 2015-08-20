package com.connect.connectcom.uberactivity.model;

import com.google.gson.annotations.SerializedName;

public class Product {

    @SerializedName("product_id") private String productId;

    @SerializedName("description") private String description;

    @SerializedName("display_name") private String displayName;

    @SerializedName("capacity") private int capacity;

    @SerializedName("image") private String image;

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "Product{" +
               "productId='" + productId + '\'' +
               ", description='" + description + '\'' +
               ", displayName='" + displayName + '\'' +
               ", capacity=" + capacity +
               ", image='" + image + '\'' +
               '}';
    }
}
