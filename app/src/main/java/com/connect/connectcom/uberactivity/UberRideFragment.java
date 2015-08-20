package com.connect.connectcom.uberactivity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.connect.connectcom.uberactivity.model.UberRide;
import com.connect.connectcom.uberactivity.presenter.UberRidesPresenter;
import com.connect.connectcom.uberactivity.routing.Navigator;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

public class UberRideFragment
        extends BaseFragment
        implements OnMapReadyCallback, UberRidesPresenter.UberRidesView {

    private static final String TAG = "UberRideFragment";
    private static final int MODE_PROGRESS = 1;
    private static final int MODE_EMPTY = 2;
    private static final int MODE_NORMAL = 3;
    private static final int MODE_NO_CONNECTIVITY = 4;
    private static final String UBER_BASE_SCHEME = "uber://";
    private static final String UBER_BASE_MOBILE_URL = "https://m.uber.com/sign-up";
    private static final String UBER_PACKAGE_NAME = "com.ubercab";

    @Bind(R.id.fragment_uber_ride_uber_list)
    ListView uberList;
    @Bind(R.id.fragment_uber_ride_progress_bar)
    ProgressBar progressBar;
    @Bind(R.id.fragment_uber_ride_main_layout)
    LinearLayout mainLayout;
    // TODO Implement a prettier empty state view
    @Bind(R.id.fragment_uber_ride_empty_view)
    TextView emptyStateTextView;
    // TODO Implement a prettier no connectivity state view
    @Bind(R.id.fragment_uber_ride_no_connectivity_view)
    TextView noConnectivityTextView;
    @Bind(R.id.fragment_uber_ride_map_view)
    MapView mapView;

    @Inject
    UberRidesPresenter uberRidesPresenter;

    private GoogleMap googleMap;
    private ArrayList<UberRide> uberRides;
    private ArrayList<Marker> markers;

    private UberListAdapter uberListAdapter;

    private boolean isPickupMarkerDrawn;
    private boolean isCameraUpdated;
    private boolean isMapReady;
    private boolean isRouteDrawn;
    private double destLongitude;
    private double destLatitude;

    public static UberRideFragment newInstance(
            double destLatitude,
            double destLongitude
    ) {
        UberRideFragment uberRideFragment = new UberRideFragment();
        Bundle bundle = new Bundle();
        bundle.putDouble(
                UberRideActivity.EXTRA_DEST_LATITUDE,
                destLatitude
        );
        bundle.putDouble(
                UberRideActivity.EXTRA_DEST_LONGITUDE,
                destLongitude
        );
        uberRideFragment.setArguments(bundle);
        return uberRideFragment;
    }

    /**
     * @param mode one of {@link UberRideFragment#MODE_EMPTY}, {@link UberRideFragment#MODE_PROGRESS}, {@link
     *             UberRideFragment#MODE_NORMAL}
     */
    private void setViewMode(int mode) {
        switch (mode) {
            case MODE_NORMAL:
                showProgress(false);
                showNormalView(true);
                showEmptyState(false);
                showNoConnectivity(false);
                break;
            case MODE_PROGRESS:
                showProgress(true);
                showNormalView(false);
                showEmptyState(false);
                showNoConnectivity(false);
                break;
            case MODE_NO_CONNECTIVITY:
                showProgress(false);
                showNormalView(false);
                showEmptyState(false);
                showNoConnectivity(true);
                break;
            case MODE_EMPTY:
                // Setting empty as the default...
            default:
                showProgress(false);
                showNormalView(false);
                showEmptyState(true);
                showNoConnectivity(false);
        }
    }

    private void showNoConnectivity(boolean show) {
        if (noConnectivityTextView != null) {
            noConnectivityTextView.setVisibility(
                    show
                            ? View.VISIBLE
                            : View.GONE
            );
        }
    }

    /**
     * HEY! If possible, please do not use this directly. Use {@link UberRideFragment#setViewMode (int)} instead
     */
    private void showNormalView(boolean show) {
        if (mainLayout != null) {
            mainLayout.setVisibility(
                    show
                            ? View.VISIBLE
                            : View.GONE
            );
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*
         * If you don't do this, the BitmapDescriptorFactory doesn't get properly initialed when
         * launching this fragment directly from a chat message context menu, even though it should
         * be, as per the official documentation...
         */
        long errorCode = MapsInitializer.initialize(getActivity());

        if (errorCode != ConnectionResult.SUCCESS) {
            // The user shouldn't have gotten this far into the app without GooglePlayServices,
            // anyway...
            Log.e(TAG, "GooglePlayServices not available");
        }

        markers = new ArrayList<>();
    }

    private void attemptUpdateMapCamera() {
        if (isMapLayoutReady() && googleMap != null && !isCameraUpdated && markers.size() > 1) {
            isCameraUpdated = true;
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            for (Marker marker : markers) {
                builder.include(marker.getPosition());
            }
            WindowManager wm = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);
            Display display = wm.getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            int width = size.x;
            int height = size.y;
            LatLngBounds latLngBounds = builder.build();
            googleMap.moveCamera(
                    CameraUpdateFactory.newLatLngBounds(
                            latLngBounds,
                            width / 4,
                            height / 4,
                            0
                    )
            );
        }
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState
    ) {
        View view = inflater.inflate(
                R.layout.uber_ride_fragment,
                container,
                false
        );
        ButterKnife.bind(
                this,
                view
        );
        mapView.onCreate(savedInstanceState);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    /**
     * HEY! If possible, please do not use this directly. Use {@link UberRideFragment#setViewMode (int)} instead
     */
    private void showProgress(boolean show) {
        if (progressBar != null) {
            progressBar.setVisibility(
                    show
                            ? View.VISIBLE
                            : View.GONE
            );
        }
    }

    /**
     * HEY! If possible, please do not use this directly. Use {@link UberRideFragment#setViewMode (int)} instead
     */
    private void showEmptyState(boolean show) {
        if (emptyStateTextView != null) {
            emptyStateTextView.setVisibility(
                    show
                            ? View.VISIBLE
                            : View.GONE
            );
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
        uberRidesPresenter.setView(null);
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        uberRidesPresenter.setView(this);

        destLatitude = getArguments().getDouble(UberRideActivity.EXTRA_DEST_LATITUDE);
        destLongitude = getArguments().getDouble(UberRideActivity.EXTRA_DEST_LONGITUDE);

        // ****************************
        // FOR DEBUGGING IN VENEZUELA - No UBER here!! :(
        // ****************************

        destLatitude = 40.737115; // Somewhere in New York
        destLongitude = -73.988746; // Somewhere in New York

        mapView.getMapAsync(this);
        uberRides = new ArrayList<>();
        uberListAdapter = new UberListAdapter(
                getActivity(),
                R.layout.uber_row_element,
                uberRides
        );
        uberListAdapter.setNotifyOnChange(true);
        uberList.setAdapter(uberListAdapter);
        uberList.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(
                            AdapterView<?> parent,
                            View view,
                            int position,
                            long id
                    ) {
                        uberRidesPresenter.onRideSelected(uberListAdapter.getItem(position));
                    }
                }
        );
    }


    private String buildUberAppQueryParams(
            String uberClientId,
            double latitude,
            double longitude,
            double destLatitude,
            double destLongitude,
            @Nullable String productId
    ) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("?client_id=").append(uberClientId);
        stringBuilder.append("&action=setPickup");
        stringBuilder.append("&pickup[latitude]=").append(latitude);
        stringBuilder.append("&pickup[longitude]=").append(longitude);
        stringBuilder.append("&dropoff[latitude]=").append(destLatitude);
        stringBuilder.append("&dropoff[longitude]=").append(destLongitude);
        stringBuilder.append("&product_id=").append(productId);
        return stringBuilder.toString();
    }

    private String buildUberMobileQueryParams(
            String uberClientId,
            double latitude,
            double longitude,
            double destLatitude,
            double destLongitude,
            @Nullable String productId
    ) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("?client_id=").append(uberClientId);
        stringBuilder.append("&pickup_latitude=").append(latitude);
        stringBuilder.append("&pickup_longitude=").append(longitude);
        stringBuilder.append("&dropoff_latitude=").append(destLatitude);
        stringBuilder.append("&dropoff_longitude=").append(destLongitude);
        stringBuilder.append("&product_id=").append(productId);
        return stringBuilder.toString();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    private boolean isMapLayoutReady() {
        return isResumed() && isMapReady;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        isMapReady = true;
        uberRidesPresenter.onMapReady(destLatitude, destLongitude);
    }


    @Override
    public void drawPickupMarker(double latitude, double longitude) {
        if (googleMap != null && !isPickupMarkerDrawn) {
            isPickupMarkerDrawn = true;
            Marker pickup = googleMap.addMarker(
                    new MarkerOptions().position(
                            new LatLng(
                                    latitude,
                                    longitude
                            )
                    ).icon(
                            BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)
                    ).title("Pickup")
            );
            markers.add(pickup);
        }
    }

    @Override
    public void drawRoute(double pickupLatitude, double pickupLongitude, double dropOffLatitude, double dropOffLongitude) {
        if (googleMap != null && !isRouteDrawn && markers.size() > 1) {
            isRouteDrawn = true;
            Navigator navigator = new Navigator(
                    googleMap,
                    new LatLng(
                            pickupLatitude,
                            pickupLongitude
                    ),
                    new LatLng(
                            dropOffLatitude,
                            dropOffLongitude
                    )
            );
            navigator.findDirections(false);
            attemptUpdateMapCamera();
        }
    }

    @Override
    public void drawDropOffMarker(double dropOffLatitude, double dropOffLongitude) {
        if (googleMap != null) {
            Marker dropoff = this.googleMap.addMarker(
                    new MarkerOptions().position(
                            new LatLng(
                                    dropOffLatitude,
                                    dropOffLongitude
                            )
                    ).title("Dropoff")
            );
            markers.add(dropoff);
            attemptUpdateMapCamera();
        }
    }

    @Override
    public void showNoConnectivity() {
        setViewMode(MODE_NO_CONNECTIVITY);
    }

    @Override
    public void showNoRides() {
        setViewMode(MODE_EMPTY);
    }

    @Override
    public void showProgress() {
        setViewMode(MODE_PROGRESS);
    }

    @Override
    public void showRides(List<UberRide> uberFusedDataItems) {
        setViewMode(MODE_NORMAL);
        uberListAdapter.clear();
        uberListAdapter.addAll(uberFusedDataItems);
    }

    @Override
    public void launchUberActivity(UberRide uberRide) {
        PackageManager packageManager = getActivity().getPackageManager();
        try {
            packageManager.getPackageInfo(
                    UBER_PACKAGE_NAME,
                    PackageManager.GET_ACTIVITIES
            );

            // Uber app is installed... launching it
            String uri = UBER_BASE_SCHEME + buildUberAppQueryParams(
                    BuildConfig.UBER_CLIENT_ID,
                    uberRide.getPickupLat(),
                    uberRide.getPickupLon(),
                    uberRide.getDropOffLat(),
                    uberRide.getDropOffLon(),
                    uberRide.getProduct().getProductId()
            );
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(uri));
            startActivity(i);

        } catch (PackageManager.NameNotFoundException e) {
            // Uber app is not installed, launching the mobile site...

            String url = UBER_BASE_MOBILE_URL + buildUberMobileQueryParams(
                    BuildConfig.UBER_CLIENT_ID,
                    uberRide.getPickupLat(),
                    uberRide.getPickupLon(),
                    uberRide.getDropOffLat(),
                    uberRide.getDropOffLon(),
                    uberRide.getProduct().getProductId()
            );
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
        }

    }

    private static class UberListAdapter
            extends ArrayAdapter<UberRide> {

        private final Context context;

        public UberListAdapter(
                Context context,
                int resource,
                List<UberRide> objects
        ) {
            super(
                    context,
                    resource,
                    objects
            );
            this.context = context;
        }

        @Override
        public View getView(
                int position,
                View convertView,
                ViewGroup parent
        ) {
            UberRide uberRide = getItem(position);
            UberDataHolder uberDataHolder;
            if (convertView != null) {
                uberDataHolder = (UberDataHolder) convertView.getTag();

            } else {
                View view = LayoutInflater.from(context).inflate(
                        R.layout.uber_row_element,
                        parent,
                        false
                );
                uberDataHolder = new UberDataHolder(view);
                view.setTag(uberDataHolder);
                convertView = view;
            }
            initDataHolder(
                    uberRide,
                    uberDataHolder
            );
            return convertView;
        }

        private void initDataHolder(
                UberRide uberRide,
                UberDataHolder uberDataHolder
        ) {
            uberDataHolder.productName.setText(uberRide.getProduct().getDisplayName());
            uberDataHolder.price.setText(getFormattedPrice(uberRide));
            uberDataHolder.time.setText(getFormattedTime(uberRide));
            Picasso.with(context).load(Uri.parse(uberRide.getProduct().getImage())).into(uberDataHolder.icon);
        }

        private String getFormattedTime(UberRide uberRide) {
            long estimate = uberRide.getTime().getEstimate();
            if (estimate > 60) {
                estimate = estimate / 60;
            } else {
                estimate = 1;
            }
            return Long.toString(estimate) + " min" + (estimate > 1
                    ? "s"
                    : "");
        }

        private String getFormattedPrice(UberRide uberRide) {
            String formattedPrice = uberRide.getPrice().getEstimate();
            if (formattedPrice == null) {
                formattedPrice = "";
            }
            return formattedPrice;
        }
    }

    static class UberDataHolder {
        @Bind(R.id.uber_row_element_product_name)
        TextView productName;
        @Bind(R.id.uber_row_element_price)
        TextView price;
        @Bind(R.id.uber_row_element_time)
        TextView time;
        @Bind(R.id.uber_row_element_icon)
        ImageView icon;

        public UberDataHolder(View view) {
            ButterKnife.bind(
                    this,
                    view
            );
        }
    }
}
