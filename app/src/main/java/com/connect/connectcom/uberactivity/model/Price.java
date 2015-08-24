package com.connect.connectcom.uberactivity.model;

import com.google.gson.annotations.SerializedName;

public class Price {

    @SerializedName("product_id")
    private String productId;

    @SerializedName("currency_code")
    private String currencyCode;//TODO: parse to java.util.Currency

    @SerializedName("display_name")
    private String displayName;

    @SerializedName("estimate")
    private String estimate;

    @SerializedName("low_estimate")
    private int lowEstimate;

    @SerializedName("high_estimate")
    private int highEstimate;

    @SerializedName("surge_multiplier")
    private float surgeMultiplier;

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getEstimate() {
        return estimate;
    }

    public void setEstimate(String estimate) {
        this.estimate = estimate;
    }

    public int getLowEstimate() {
        return lowEstimate;
    }

    public void setLowEstimate(int lowEstimate) {
        this.lowEstimate = lowEstimate;
    }

    public int getHighEstimate() {
        return highEstimate;
    }

    public void setHighEstimate(int highEstimate) {
        this.highEstimate = highEstimate;
    }

    public float getSurgeMultiplier() {
        return surgeMultiplier;
    }

    public void setSurgeMultiplier(float surgeMultiplier) {
        this.surgeMultiplier = surgeMultiplier;
    }

    @Override
    public String toString() {
        return "Price{" +
                "productId='" + productId + '\'' +
                ", currencyCode='" + currencyCode + '\'' +
                ", displayName='" + displayName + '\'' +
                ", estimate='" + estimate + '\'' +
                ", lowEstimate=" + lowEstimate +
                ", highEstimate=" + highEstimate +
                ", surgeMultiplier=" + surgeMultiplier +
                '}';
    }
}
