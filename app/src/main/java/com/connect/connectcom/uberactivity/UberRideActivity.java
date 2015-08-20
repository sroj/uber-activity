package com.connect.connectcom.uberactivity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

public class UberRideActivity
        extends BaseActivity {

    public static final String EXTRA_DEST_LATITUDE = "com.connect.connectcom.EXTRA_DEST_LATITUDE";
    public static final String EXTRA_DEST_LONGITUDE = "com.connect.connectcom.EXTRA_DEST_LONGITUDE";
    private static final String TAG_FRAGMENT_UBER_RIDE = "com.connect.connectcom.TAG_FRAGMENT_UBER_RIDE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.uber_ride_activity);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FragmentManager fragmentManager = getFragmentManager();

        Fragment uberRideFragment = fragmentManager.findFragmentByTag(TAG_FRAGMENT_UBER_RIDE);

        if (uberRideFragment == null) {
            uberRideFragment = UberRideFragment.newInstance(
                    getIntent().getDoubleExtra(
                            EXTRA_DEST_LATITUDE,
                            0
                    ),
                    getIntent().getDoubleExtra(
                            EXTRA_DEST_LONGITUDE,
                            0
                    )
            );
            fragmentManager.beginTransaction().add(
                    R.id.activity_uber_ride_root_container,
                    uberRideFragment,
                    TAG_FRAGMENT_UBER_RIDE
            ).commit();
        }
    }

    public static Intent buildDefaultIntent(
            Context context,
            double destLatitude,
            double destLongitude
    ) {
        Intent intent = new Intent(
                context,
                UberRideActivity.class
        );
        intent.putExtra(
                EXTRA_DEST_LATITUDE,
                destLatitude
        );
        intent.putExtra(
                EXTRA_DEST_LONGITUDE,
                destLongitude
        );
        return intent;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
