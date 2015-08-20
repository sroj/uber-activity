package com.connect.connectcom.uberactivity.model;

import com.google.gson.annotations.SerializedName;

public class Time {

    @SerializedName("product_id") private String productId;

    @SerializedName("display_name") private String displayName;

    @SerializedName("estimate") private int estimate;

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public int getEstimate() {
        return estimate;
    }

    public void setEstimate(int estimate) {
        this.estimate = estimate;
    }

    @Override
    public String toString() {
        return "Time{" +
               "productId='" + productId + '\'' +
               ", displayName='" + displayName + '\'' +
               ", estimate=" + estimate +
               '}';
    }
}
