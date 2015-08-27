package com.connect.connectcom.uberactivity.view;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
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

import com.connect.connectcom.uberactivity.BuildConfig;
import com.connect.connectcom.uberactivity.R;
import com.connect.connectcom.uberactivity.model.UberRide;
import com.connect.connectcom.uberactivity.presenter.UberRidesPresenter;
import com.connect.connectcom.uberactivity.routing.MapRouteDrawer;
import com.connect.connectcom.uberactivity.util.Ln;
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
import mortar.dagger1support.ObjectGraphService;

/**
 * Created by Simon on 8/22/2015.
 */
public class UberRidesViewImpl extends UberRidesPresenter.UberRidesView implements OnMapReadyCallback {

    // **** Constants ****
    private static final int MODE_PROGRESS = 1;
    private static final int MODE_EMPTY = 2;
    private static final int MODE_NORMAL = 3;
    private static final int MODE_NO_CONNECTIVITY = 4;
    private static final String UBER_BASE_SCHEME = "uber://";
    private static final String UBER_BASE_MOBILE_URL = "https://m.uber.com/sign-up";
    private static final String UBER_PACKAGE_NAME = "com.ubercab";

    // **** Child views ****
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


    // **** UI and Business logic ****
    @Inject
    UberRidesPresenter uberRidesPresenter;
    @Inject
    MapRouteDrawer mapRouteDrawer;

    private GoogleMap googleMap;
    private ArrayList<UberRide> uberRides;
    private ArrayList<Marker> markers;

    private UberListAdapter uberListAdapter;

    private boolean isPickupMarkerDrawn;
    private boolean isCameraUpdated;
    private boolean isMapReady;
    private boolean isRouteDrawn;
    private Double destLongitude;
    private Double destLatitude;
    private UberRidesViewContainer uberRidesViewContainer;
    private boolean markersDrawn;

    public UberRidesViewImpl(Context context) {
        super(context);
    }

    public UberRidesViewImpl(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public UberRidesViewImpl(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this, this);
        ObjectGraphService.inject(getContext(), this);
        initView();
    }

    private void initView() {
        /*
         * If you don't do this, the BitmapDescriptorFactory doesn't get properly initialed when
         * launching this fragment directly from a chat message context menu, even though it should
         * be, as per the official documentation...
         */
        long errorCode = MapsInitializer.initialize(getContext());

        if (errorCode != ConnectionResult.SUCCESS) {
            // The user shouldn't have gotten this far into the app without GooglePlayServices,
            // anyway...
            Ln.e("GooglePlayServices not available");
        }

        markers = new ArrayList<>();

        mapView.getMapAsync(this);
        uberRides = new ArrayList<>();
        uberListAdapter = new UberListAdapter(getContext(), R.layout.uber_row_element, uberRides);
        uberListAdapter.setNotifyOnChange(true);
        uberList.setAdapter(uberListAdapter);
        uberList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                uberRidesPresenter.onRideSelected(uberListAdapter.getItem(position));
            }
        });
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        uberRidesViewContainer = (UberRidesViewContainer) getContext();
        destLatitude = uberRidesViewContainer.getDestLatitude();
        destLongitude = uberRidesViewContainer.getDestLongitude();
        uberRidesViewContainer.onUberViewAttachedToWindow(this);
        uberRidesPresenter.takeView(this);
        attemptDrawMapMarkers();
    }

    private void attemptDrawMapMarkers() {
        if (isMapReady && destLatitude != null && destLongitude != null && !markersDrawn) {
            markersDrawn = true;
            uberRidesPresenter.onMapReady(destLatitude, destLongitude);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        uberRidesViewContainer.onUberViewDetachedFromWindow();
        uberRidesViewContainer = null;
        uberRidesPresenter.dropView(this);
    }

    private void attemptUpdateMapCamera() {
        if (isMapLayoutReady() && googleMap != null && !isCameraUpdated && markers.size() > 1) {
            isCameraUpdated = true;
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            for (Marker marker : markers) {
                builder.include(marker.getPosition());
            }
            WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
            Display display = wm.getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            int width = size.x;
            int height = size.y;
            LatLngBounds latLngBounds = builder.build();
            googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, width / 4, height / 4, 0));
        }
    }

    /**
     * @param mode one of {@link UberRidesViewImpl#MODE_EMPTY}, {@link UberRidesViewImpl#MODE_PROGRESS}, {@link
     *             UberRidesViewImpl#MODE_NORMAL}
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
            noConnectivityTextView.setVisibility(show ? View.VISIBLE : View.GONE);
        }
    }

    /**
     * HEY! If possible, please do not use this directly. Use {@link UberRidesViewImpl#setViewMode (int)} instead
     */
    private void showNormalView(boolean show) {
        if (mainLayout != null) {
            mainLayout.setVisibility(show ? View.VISIBLE : View.GONE);
        }
    }

    /**
     * HEY! If possible, please do not use this directly. Use {@link UberRidesViewImpl#setViewMode (int)} instead
     */
    private void showProgress(boolean show) {
        if (progressBar != null) {
            progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        }
    }

    /**
     * HEY! If possible, please do not use this directly. Use {@link UberRidesViewImpl#setViewMode (int)} instead
     */
    private void showEmptyState(boolean show) {
        if (emptyStateTextView != null) {
            emptyStateTextView.setVisibility(show ? View.VISIBLE : View.GONE);
        }
    }

    private boolean isMapLayoutReady() {
        return !((AppCompatActivity) getContext()).isFinishing() && isMapReady;
    }

    @Override
    public void drawPickupMarker(double latitude, double longitude) {
        if (googleMap != null && !isPickupMarkerDrawn) {
            isPickupMarkerDrawn = true;
            Marker pickup = googleMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).icon(
                    BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)).title("Pickup"));
            markers.add(pickup);
        }
    }

    @Override
    public void drawRoute(double pickupLatitude,
                          double pickupLongitude,
                          double dropOffLatitude,
                          double dropOffLongitude) {
        if (googleMap != null && !isRouteDrawn && markers.size() > 1) {
            isRouteDrawn = true;

            mapRouteDrawer.drawRoute(googleMap,
                                     new LatLng(pickupLatitude, pickupLongitude),
                                     new LatLng(dropOffLatitude, dropOffLongitude));

            attemptUpdateMapCamera();
        }
    }

    @Override
    public void drawDropOffMarker(double dropOffLatitude, double dropOffLongitude) {
        if (googleMap != null) {
            Marker dropoff = this.googleMap.addMarker(new MarkerOptions().position(new LatLng(dropOffLatitude,
                                                                                              dropOffLongitude)).title(
                    "Dropoff"));
            markers.add(dropoff);
            attemptUpdateMapCamera();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        mapView.onCreate(savedInstanceState);
    }


    @Override
    public void onResume() {
        mapView.onResume();
    }

    @Override
    public void onPause() {
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        mapView.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory() {
        mapView.onLowMemory();
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
        PackageManager packageManager = getContext().getPackageManager();
        try {
            packageManager.getPackageInfo(UBER_PACKAGE_NAME, PackageManager.GET_ACTIVITIES);

            // Uber app is installed... launching it
            String uri = UBER_BASE_SCHEME + buildUberAppQueryParams(BuildConfig.UBER_CLIENT_ID,
                                                                    uberRide.getPickupLat(),
                                                                    uberRide.getPickupLon(),
                                                                    uberRide.getDropOffLat(),
                                                                    uberRide.getDropOffLon(),
                                                                    uberRide.getProduct().getProductId());
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(uri));
            getContext().startActivity(i);

        } catch (PackageManager.NameNotFoundException e) {
            // Uber app is not installed, launching the mobile site...

            String url = UBER_BASE_MOBILE_URL + buildUberMobileQueryParams(BuildConfig.UBER_CLIENT_ID,
                                                                           uberRide.getPickupLat(),
                                                                           uberRide.getPickupLon(),
                                                                           uberRide.getDropOffLat(),
                                                                           uberRide.getDropOffLon(),
                                                                           uberRide.getProduct().getProductId());
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            getContext().startActivity(i);
        }

    }

    private String buildUberAppQueryParams(String uberClientId,
                                           double latitude,
                                           double longitude,
                                           double destLatitude,
                                           double destLongitude,
                                           @Nullable String productId) {
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

    private String buildUberMobileQueryParams(String uberClientId,
                                              double latitude,
                                              double longitude,
                                              double destLatitude,
                                              double destLongitude,
                                              @Nullable String productId) {
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
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        isMapReady = true;
        attemptDrawMapMarkers();
    }

    private static class UberListAdapter extends ArrayAdapter<UberRide> {

        private final Context context;

        public UberListAdapter(Context context, int resource, List<UberRide> objects) {
            super(context, resource, objects);
            this.context = context;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            UberRide uberRide = getItem(position);
            UberDataHolder uberDataHolder;
            if (convertView != null) {
                uberDataHolder = (UberDataHolder) convertView.getTag();

            } else {
                View view = LayoutInflater.from(context).inflate(R.layout.uber_row_element, parent, false);
                uberDataHolder = new UberDataHolder(view);
                view.setTag(uberDataHolder);
                convertView = view;
            }
            initDataHolder(uberRide, uberDataHolder);
            return convertView;
        }

        private void initDataHolder(UberRide uberRide, UberDataHolder uberDataHolder) {
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
            return Long.toString(estimate) + " min" + (estimate > 1 ? "s" : "");
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
            ButterKnife.bind(this, view);
        }
    }
}
