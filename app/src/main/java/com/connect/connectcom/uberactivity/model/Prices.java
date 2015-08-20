package com.connect.connectcom.uberactivity.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Prices {

    @SerializedName("prices") private List<Price> prices;
    private boolean ok = true;

    public List<Price> getPrices() {
        return prices;
    }

    public void setPrices(List<Price> prices) {
        this.prices = prices;
    }

    public boolean isOk() {
        return ok;
    }

    public static Prices buildErrorObject() {
        Prices prices = new Prices();
        prices.ok = false;
        return prices;
    }

    @Override
    public String toString() {
        return "Prices{" +
               "prices=" + prices +
               '}';
    }
}
