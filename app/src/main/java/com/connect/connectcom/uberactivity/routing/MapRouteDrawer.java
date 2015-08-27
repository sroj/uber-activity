package com.connect.connectcom.uberactivity.routing;

import android.graphics.Color;

import com.connect.connectcom.uberactivity.backend.GoogleDirectionsAPI;
import com.connect.connectcom.uberactivity.backend.model.DirectionsGETResponse;
import com.connect.connectcom.uberactivity.util.Ln;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by Simon on 8/26/2015.
 */
@Singleton
public class MapRouteDrawer {

    private static final int PATH_COLOR = Color.parseColor("#21b6d7"); // Connect blue
    private static final float PATH_WIDTH = 8;

    @Inject
    GoogleDirectionsAPI googleDirectionsAPI;


    public void drawRoute(final GoogleMap googleMap, LatLng start, LatLng end) {
        googleDirectionsAPI.getDirections(formatLatLng(start), formatLatLng(end)).
                observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<DirectionsGETResponse>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Ln.e(e, "Error calling the Google Directions API");
            }

            @Override
            public void onNext(DirectionsGETResponse directionsGETResponse) {
                Ln.d(directionsGETResponse);
                drawRoute(googleMap, directionsGETResponse);
            }
        });

    }

    private void drawRoute(GoogleMap googleMap, DirectionsGETResponse directionsGETResponse) {
        if (directionsGETResponse == null || directionsGETResponse.getRoutes() == null || directionsGETResponse.getRoutes().isEmpty()) {
            return;
        }

        // The points that delimit the polylines that will ultimately be drawn
        List<LatLng> latLngs = new ArrayList<>();

        // We are interested in just one route
        final List<DirectionsGETResponse.Route.Leg> legs = directionsGETResponse.getRoutes().get(0).getLegs();

        if (legs == null || legs.isEmpty()) {
            return;
        }

        for (DirectionsGETResponse.Route.Leg leg : legs) {
            final List<DirectionsGETResponse.Route.Leg.Step> steps = leg.getSteps();
            if (steps == null) {
                continue;
            }
            for (DirectionsGETResponse.Route.Leg.Step step : steps) {
                latLngs.addAll(step.getPolyline().getDecodedPoints());
            }
        }

        googleMap.addPolyline(new PolylineOptions().addAll(latLngs).color(PATH_COLOR).width(PATH_WIDTH));
    }

    private String formatLatLng(LatLng latLng) {
        return latLng.latitude + "," + latLng.longitude;
    }
}
