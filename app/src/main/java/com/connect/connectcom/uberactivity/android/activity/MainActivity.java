package com.connect.connectcom.uberactivity.android.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;

import com.connect.connectcom.uberactivity.R;
import com.connect.connectcom.uberactivity.flow.GsonParceler;
import com.connect.connectcom.uberactivity.flow.LandingScreen;
import com.connect.connectcom.uberactivity.presenter.UberRidesPresenter;
import com.connect.connectcom.uberactivity.util.Ln;
import com.google.gson.Gson;

import flow.Flow;
import flow.Flow.Dispatcher;
import flow.FlowDelegate;
import flow.History;

public class MainActivity extends BaseActivity implements UberRidesPresenter.UberRidesView.UberRidesViewContainer, Dispatcher {

    private UberRidesPresenter.UberRidesView uberRidesView;

    private FlowDelegate flowDelegate;
    private Dispatcher dispatcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FlowDelegate.NonConfigurationInstance nonConfig = (FlowDelegate.NonConfigurationInstance) getLastCustomNonConfigurationInstance();
        dispatcher = (Dispatcher) findViewById(R.id.main_activity_container);

        flowDelegate = FlowDelegate.onCreate(nonConfig,
                                             getIntent(),
                                             savedInstanceState,
                                             new GsonParceler(new Gson()),
                                             History.single(new LandingScreen()),
                                             this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        flowDelegate.onResume();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public Object getSystemService(String name) {
        Object service = null;
        if (flowDelegate != null) {
            service = flowDelegate.getSystemService(name);
        }
        return service != null ? service : super.getSystemService(name);
    }

    @Override
    public double getDestLatitude() {
        // Hardcoded for debugging in Venezuela
        return 40.786338;
    }

    @Override
    public double getDestLongitude() {
        // Hardcoded for debugging in Venezuela
        return -74.018170;
    }

    @Override
    public void onBackPressed() {
        if (flowDelegate.onBackPressed()) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        flowDelegate.onNewIntent(intent);
    }


    @Override
    public Object onRetainCustomNonConfigurationInstance() {
        return flowDelegate.onRetainNonConfigurationInstance();
    }

    @Override
    public void onUberViewAttachedToWindow(UberRidesPresenter.UberRidesView uberRidesView) {
        callLifecycleMethods(uberRidesView);
    }

    @Override
    public void onUberViewDetachedFromWindow() {
        uberRidesView = null;
    }

    @Override
    protected void onPause() {
        super.onPause();

        flowDelegate.onPause();
        if (uberRidesView != null) {
            uberRidesView.onPause();
        }
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        if (uberRidesView != null) {
            uberRidesView.onLowMemory();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (uberRidesView != null) {
            uberRidesView.onDestroy();
            uberRidesView = null;
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (uberRidesView != null) {
            uberRidesView.onSaveInstanceState(outState);
        }
        flowDelegate.onSaveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    private void callLifecycleMethods(UberRidesPresenter.UberRidesView uberRidesView) {
        this.uberRidesView = uberRidesView;
        if (uberRidesView != null) {
            uberRidesView.onCreate(null);
            uberRidesView.onResume();
        }
    }

    @Override
    public void dispatch(Flow.Traversal traversal, final Flow.TraversalCallback callback) {
        Ln.d("Dispatch called");
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            boolean canGoBack = traversal.destination.size() > 1;
            actionBar.setHomeButtonEnabled(canGoBack);
            actionBar.setDisplayHomeAsUpEnabled(canGoBack);
        }
        dispatcher.dispatch(traversal, new Flow.TraversalCallback() {
            @Override
            public void onTraversalCompleted() {
                callback.onTraversalCompleted();
            }
        });
    }
}
