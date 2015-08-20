package com.connect.connectcom.uberactivity;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

/**
 * Created by bsechter on 2014/10/23.
 * <p/>
 * Reference: http://stackoverflow.com/questions/17519198/how-to-get-the-current-location
 * -latitude-and-longitude-in-android
 * <p/>
 * Create this Class from tutorial : http://www.androidhive .info/2012/07/android-gps-location-manager-tutorial
 * <p/>
 * For Geocoder: http://stackoverflow.com/questions/472313/android-reverse-geocoding-getfromlocation
 */

public class GPSTracker
        implements LocationListener {
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; //10 meters
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60; // 1 minute
    private final Context mContext;
    protected LocationManager locationManager;
    boolean isGPSEnabled = false;
    boolean isNetworkEnabled = false;
    boolean canGetLocation = false;
    Location location;
    double latitude;
    double longitude;

    public GPSTracker(Context context) {
        this.mContext = context;
        init();
    }

    private void init() {
        try {
            locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSEnabled && !isNetworkEnabled) {
                location = new Location("");
                updateGPSCoordinates();
            } else {
                this.canGetLocation = true;

                //First get location from Network Provider
                if (isNetworkEnabled) {
                    locationManager.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES,
                            this
                    );

                    if (locationManager != null) {
                        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        updateGPSCoordinates();
                    }
                }

                //if GPS Enabled get lat/long using GPS Services
                if (isGPSEnabled) {
                    if (location == null) {
                        locationManager.requestLocationUpdates(
                                LocationManager.GPS_PROVIDER,
                                MIN_TIME_BW_UPDATES,
                                MIN_DISTANCE_CHANGE_FOR_UPDATES,
                                this
                        );

                        if (locationManager != null) {
                            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            updateGPSCoordinates();
                        }
                    }
                }
            }
        } catch (Exception e) {
            //e.printStackTrace();
            Ln.e(
                    e,
                    "Impossible to connect to LocationManager"
            );
            location = new Location("");
            updateGPSCoordinates();
        }
    }

    public Location getLocation() {
        return location;
    }

    public void updateGPSCoordinates() {
        if (location != null) {
            latitude = location.getLatitude();
            longitude = location.getLongitude();
        }
    }

    /**
     * Stop using GPS listener Calling this function will stop using GPS in your app
     */

    public void stopUsingGPS() {
        if (locationManager != null) {
            locationManager.removeUpdates(GPSTracker.this);
        }
    }

    /**
     * Function to get latitude
     */
    public double getLatitude() {
        if (location != null) {
            latitude = location.getLatitude();
        }

        return latitude;
    }

    /**
     * Function to get longitude
     */
    public double getLongitude() {
        if (location != null) {
            longitude = location.getLongitude();
        }

        return longitude;
    }

    @Override
    public void onLocationChanged(Location location) {
        this.location = location;
        updateGPSCoordinates();
    }

    @Override
    public void onStatusChanged(
            String provider,
            int status,
            Bundle extras
    ) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    //    /**
    //     * Function to check GPS/wifi enabled
    //     */
    //    public boolean canGetLocation() {
    //        return this.canGetLocation;
    //    }
    //
    //    /**
    //     * Get list of address by latitude and longitude
    //     *
    //     * @return null or List<Address>
    //     */
    //    public List<Address> getGeocoderAddress(Context context) {
    //        if (location != null) {
    //            Geocoder geocoder = new Geocoder(
    //                context,
    //                Locale.ENGLISH
    //            );
    //            try {
    //                List<Address> addresses = geocoder.getFromLocation(
    //                    latitude,
    //                    longitude,
    //                    1
    //                );
    //                return addresses;
    //            }
    //            catch (IOException e) {
    //                //e.printStackTrace();
    //                Log.e(
    //                    "Error : Geocoder",
    //                    "Impossible to connect to Geocoder",
    //                    e
    //                );
    //            }
    //        }
    //
    //        return null;
    //    }

    //    /**
    //    * Try to get AddressLine
    //    *
    //        * @return null or addressLine
    //    */
    //    public String getAddressLine(Context context) {
    //        List<Address> addresses = getGeocoderAddress(context);
    //        if (addresses != null && addresses.size() > 0) {
    //            Address address = addresses.get(0);
    //            String addressLine = address.getAddressLine(0);
    //
    //            return addressLine;
    //        }
    //        else {
    //            return null;
    //        }
    //    }
    //
    //    /**
    //     * Try to get Locality
    //     *
    //     * @return null or locality
    //     */
    //    public String getLocality(Context context) {
    //        List<Address> addresses = getGeocoderAddress(context);
    //        if (addresses != null && addresses.size() > 0) {
    //            Address address = addresses.get(0);
    //            String locality = address.getLocality();
    //
    //            return locality;
    //        }
    //        else {
    //            return null;
    //        }
    //    }
    //
    //    /**
    //     * Try to get Postal Code
    //     *
    //     * @return null or postalCode
    //     */
    //    public String getPostalCode(Context context) {
    //        List<Address> addresses = getGeocoderAddress(context);
    //        if (addresses != null && addresses.size() > 0) {
    //            Address address = addresses.get(0);
    //            String postalCode = address.getPostalCode();
    //
    //            return postalCode;
    //        }
    //        else {
    //            return null;
    //        }
    //    }
    //
    //    /**
    //     * Try to get CountryName
    //     *
    //     * @return null or postalCode
    //     */
    //    public String getCountryName(Context context) {
    //        List<Address> addresses = getGeocoderAddress(context);
    //        if (addresses != null && addresses.size() > 0) {
    //            Address address = addresses.get(0);
    //            String countryName = address.getCountryName();
    //
    //            return countryName;
    //        }
    //        else {
    //            return null;
    //        }
    //    }
    //
    //    public static String getCountryCodeGivenLocation(
    //        Context context,
    //        double lat,
    //        double lon
    //    ) {
    //        Geocoder geocoder = new Geocoder(
    //            context,
    //            Locale.ENGLISH
    //        );
    //        List<Address> addresses = null;
    //        Address address = null;
    //        String countryCode = null;
    //        try {
    //            addresses = geocoder.getFromLocation(
    //                lat,
    //                lon,
    //                1
    //            );
    //            address = addresses.get(0);
    //            countryCode = address.getCountryCode();
    //        }
    //        catch (IOException e) {
    //            //e.printStackTrace();
    //            Log.e(
    //                "Error : Geocoder",
    //                "Impossible to connect to Geocoder",
    //                e
    //            );
    //        }
    //        String country = countryCode != null
    //                         ? countryCode
    //                         : "us";
    //
    //        return country;
    //    }
}
