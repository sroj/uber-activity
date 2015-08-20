package com.connect.connectcom.uberactivity;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by Simon on 5/29/2015.
 */
@Singleton
public class FusedLocationManager {
    private final Context context;

    private Location locationLastBest = null;

    @Inject
    public FusedLocationManager(Context context) {
        this.context = context;
    }

    public Location getLastBestLocation() {
        return locationLastBest;
    }

    private Location getLastKnownLocation() throws
            Exception {
        if (GooglePlayServicesUtil.isGooglePlayServicesAvailable(context) == ConnectionResult.SUCCESS) {
            GoogleApiClient googleApiClient = null;
            try {
                googleApiClient = new GoogleApiClient.Builder(context).addApi(
                        LocationServices.API
                ).build();
                ConnectionResult connectionResult = googleApiClient.blockingConnect(
                        2,
                        TimeUnit.MINUTES
                );
                if (connectionResult.getErrorCode() == ConnectionResult.SUCCESS) {
                    Location lastLocation = LocationServices.FusedLocationApi.getLastLocation(
                            googleApiClient
                    );
                    return locationLastBest = lastLocation;
                } else {
                    throw new Exception("Could not connect to Google API Client");
                }
            } finally {
                if (googleApiClient != null) {
                    googleApiClient.disconnect();
                }
            }
        } else {
            // If Google Play Services is unavailable or can't be connected, use the legacy Location Manager
            // as a fallback in order to get user's current location
            // NOTE: GPSTracker does not throw any exceptions, it will return a new Location("") if it cannot
            // get the user's location, we may want to refactor this to be consistent
            GPSTracker gpsTracker = new GPSTracker(context);
            gpsTracker.stopUsingGPS();
            return locationLastBest = gpsTracker.getLocation();
        }
    }

    public Observable<Location> getLastKnownLocationObservable() {
        return Observable.create(
                new Observable.OnSubscribe<Location>() {
                    @Override
                    public void call(Subscriber<? super Location> subscriber) {
                        try {
                            subscriber.onNext(getLastKnownLocation());
                            subscriber.onCompleted();
                        } catch (Exception e) {
                            subscriber.onError(e);
                        }
                    }
                }
        ).subscribeOn(Schedulers.io());
    }

    public Observable<Location> getLocationWithFrequency(Frequency frequency) {
        return Observable.timer(
                0L,
                frequency.getTimeBetweenPolling(),
                TimeUnit.MILLISECONDS
        ).flatMap(
                new Func1<Long, Observable<? extends Location>>() {
                    @Override
                    public Observable<? extends Location> call(Long unused) {
                        Log.d("FusedLocationManager", "RTLSERVICE locationObservable.onCall()");
                        return getLastKnownLocationObservable();
                    }
                }
        );
    }

    public final Observable<String> getCityWithLatitudeAndLongitude(
            final float latitude,
            final float longitude
    ) {
        return Observable.create(
                new Observable.OnSubscribe<String>() {
                    @Override
                    public void call(Subscriber<? super String> subscriber) {
                        Geocoder geocoder = new Geocoder(
                                context,
                                Locale.getDefault()
                        );
                        List<Address> addresses = null;
                        try {
                            addresses = geocoder.getFromLocation(
                                    latitude,
                                    longitude,
                                    1
                            );
                        } catch (IllegalArgumentException | IOException e) {
                            subscriber.onError(e);
                        }
                        if (addresses != null && addresses.size() > 0) {
                            Address address = addresses.get(0);
                            String addressText = String.format(
                                    "%s, %s, %s",
                                    address.getLocality(),
                                    address.getAdminArea(),
                                    address.getCountryName()
                            );
                            subscriber.onNext(addressText);
                        } else {
                            subscriber.onNext("No address found");
                        }
                        subscriber.onCompleted();
                    }
                }
        ).subscribeOn(Schedulers.io());
    }

    public enum Frequency {
        STANDARD_MAP(120000L), // 2 minutes
        REAL_TIME(5000L); // 5 seconds

        private final long timeBetweenPolling;

        private Frequency(long timeBetweenPolling) {
            this.timeBetweenPolling = timeBetweenPolling;
        }

        public long getTimeBetweenPolling() {
            return timeBetweenPolling;
        }
    }
}
