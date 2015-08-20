package com.connect.connectcom.uberactivity.model;

/**
 * Created by Simon on 8/18/2015.
 */
public class UberRide {

    private Price price;
    private Time time;
    private Product product;

    private double dropOffLat;
    private double dropOffLon;
    private double pickupLat;
    private double pickupLon;


    public Price getPrice() {
        return price;
    }

    public void setPrice(Price price) {
        this.price = price;
    }

    public Time getTime() {
        return time;
    }

    public void setTime(Time time) {
        this.time = time;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public double getDropOffLat() {
        return dropOffLat;
    }

    public void setDropOffLat(double dropOffLat) {
        this.dropOffLat = dropOffLat;
    }

    public double getDropOffLon() {
        return dropOffLon;
    }

    public void setDropOffLon(double dropOffLon) {
        this.dropOffLon = dropOffLon;
    }

    public double getPickupLat() {
        return pickupLat;
    }

    public void setPickupLat(double pickupLat) {
        this.pickupLat = pickupLat;
    }

    public double getPickupLon() {
        return pickupLon;
    }

    public void setPickupLon(double pickupLon) {
        this.pickupLon = pickupLon;
    }

    @Override
    public String toString() {
        return "UberFusedDataItem{" +
                "price=" + price +
                ", time=" + time +
                ", product=" + product +
                '}';
    }
}
