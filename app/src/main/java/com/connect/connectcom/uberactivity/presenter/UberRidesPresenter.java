package com.connect.connectcom.uberactivity.presenter;

import com.connect.connectcom.uberactivity.model.UberRide;

import java.util.List;

/**
 * Created by Simon on 8/18/2015.
 */

public interface UberRidesPresenter {
    void onRideSelected(UberRide uberRide);

    void loadUberRides(double dropOffLatitude, double dropOffLongitude);

    void onMapReady(double dropOffLatitude, double dropOffLongitude);

    void setView(UberRidesView view);

    /**
     * This is the view the presenter will interact with
     */
    interface UberRidesView {

        void showNoConnectivity();

        void showNoRides();

        void showProgress();

        void showRides(List<UberRide> uberFusedDataItems);

        void launchUberActivity(UberRide uberRide);

        void drawPickupMarker(double latitude, double longitude);

        void drawRoute(double pickupLatitude,
                       double pickupLongitude,
                       double dropOffLatitude,
                       double dropOffLongitude);

        void drawDropOffMarker(double dropOffLatitude, double dropOffLongitude);
    }
}
