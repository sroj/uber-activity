package com.connect.connectcom.uberactivity.presenter;

import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.connect.connectcom.uberactivity.model.UberRide;

import java.util.List;

import mortar.ViewPresenter;

/**
 * Created by Simon on 8/18/2015.
 */

public abstract class UberRidesPresenter extends ViewPresenter<UberRidesPresenter.UberRidesView> {
    public abstract void onRideSelected(UberRide uberRide);

    public abstract void loadUberRides(double dropOffLatitude, double dropOffLongitude);

    public abstract void onMapReady(double dropOffLatitude, double dropOffLongitude);

    /**
     * This is the view the presenter will interact with
     */
    public abstract static class UberRidesView extends LinearLayout {

        public UberRidesView(Context context) {
            super(context);
        }

        public UberRidesView(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        public UberRidesView(Context context, AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
        }

        public abstract void showNoConnectivity();

        public abstract void showNoRides();

        public abstract void showProgress();

        public abstract void showRides(List<UberRide> uberFusedDataItems);

        public abstract void launchUberActivity(UberRide uberRide);

        public abstract void drawPickupMarker(double latitude, double longitude);

        public abstract void drawRoute(double pickupLatitude,
                                       double pickupLongitude,
                                       double dropOffLatitude,
                                       double dropOffLongitude);

        public abstract void drawDropOffMarker(double dropOffLatitude, double dropOffLongitude);

        public abstract void onCreate(Bundle savedInstanceState);

        public abstract void onResume();

        public abstract void onPause();

        public abstract void onDestroy();

        public abstract void onSaveInstanceState(Bundle outState);

        public abstract void onLowMemory();

        public interface UberRidesViewContainer {
            double getDestLatitude();

            double getDestLongitude();

            void onUberViewAttachedToWindow(UberRidesView uberRidesView);

            void onUberViewDetachedFromWindow();
        }
    }
}
