package com.connect.connectcom.uberactivity.presenter;

import android.location.Location;
import android.os.Build;

import com.connect.connectcom.uberactivity.module.BaseActivityModule;
import com.connect.connectcom.uberactivity.BuildConfig;
import com.connect.connectcom.uberactivity.location.FusedLocationManager;
import com.connect.connectcom.uberactivity.backend.UberAPI;
import com.connect.connectcom.uberactivity.model.UberRide;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.Module;
import dagger.ObjectGraph;
import dagger.Provides;

/**
 * Created by Simon on 8/18/2015.
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP)
public class UberRidesPresenterTest {

    @Inject
    UberAPI uberAPI;

    @Inject
    FusedLocationManager fusedLocationManager;


    @Before
    public void setUp() throws Exception {

        ObjectGraph.create(new TestModule()).inject(this);

    }

    @Test
    public void testOnRideSelected() throws Exception {

        UberRidesPresenter uberRidesPresenter = new UberRidesPresenterImpl(fusedLocationManager, uberAPI);

        UberRidesPresenter.UberRidesView uberRidesView = Mockito.mock(UberRidesPresenter.UberRidesView.class);
        UberRide uberRide = new UberRide();
        uberRidesPresenter.takeView(uberRidesView);

        uberRidesPresenter.onRideSelected(uberRide);

        Mockito.verify(uberRidesView).launchUberActivity(uberRide);
    }

    @Test
    public void testLoadUberRides() throws Exception {

    }

    @Test
    public void testOnMapReady() throws Exception {
        UberRidesPresenter uberRidesPresenter = new UberRidesPresenterImpl(fusedLocationManager, uberAPI);

        Mockito.when(fusedLocationManager.getLastKnownLocationObservable()).thenReturn(rx.Observable.<Location>just(null));

        UberRidesPresenter.UberRidesView uberRidesView = Mockito.mock(UberRidesPresenter.UberRidesView.class);
        uberRidesPresenter.takeView(uberRidesView);

        uberRidesPresenter.onMapReady(10, 20);

        Mockito.verify(uberRidesView).showNoRides();
    }

    @Test
    public void testSetView() throws Exception {

    }

    @Module(
            includes = BaseActivityModule.class,
            injects = UberRidesPresenterTest.class,
            overrides = true
    )
    static class TestModule {

        @Provides
        @Singleton
        UberAPI provideMockUberAPI() {
            return Mockito.mock(UberAPI.class);
        }

        @Provides
        @Singleton
        FusedLocationManager provideMockFusedLocationManager() {
            return Mockito.mock(FusedLocationManager.class);
        }
    }
}