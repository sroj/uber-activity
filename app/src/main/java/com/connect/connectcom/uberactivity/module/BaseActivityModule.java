package com.connect.connectcom.uberactivity.module;

import com.connect.connectcom.uberactivity.BuildConfig;
import com.connect.connectcom.uberactivity.backend.UberAPI;
import com.connect.connectcom.uberactivity.location.FusedLocationManager;
import com.connect.connectcom.uberactivity.presenter.MainPresenter;
import com.connect.connectcom.uberactivity.presenter.MainPresenterImpl;
import com.connect.connectcom.uberactivity.presenter.UberRidesPresenter;
import com.connect.connectcom.uberactivity.presenter.UberRidesPresenterImpl;
import com.connect.connectcom.uberactivity.util.Constants;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;

/**
 * Created by sven on 6/29/15.
 */
@Module(
        library = true,
        complete = false)
public final class BaseActivityModule {

    @Provides
    @Singleton
    @Named("UberRequestInterceptor")
    RequestInterceptor provideUberRequestInterceptor() {
        return new RequestInterceptor() {
            @Override
            public void intercept(RequestInterceptor.RequestFacade request) {
                request.addHeader(Constants.AUTHORIZATION_HEADER_NAME, BuildConfig.UBER_SERVER_KEY);
            }
        };
    }

    @Provides
    @Singleton
    @Named("UberAPIAdapter")
    RestAdapter provideUberAPIAdapter(@Named("UberRequestInterceptor") RequestInterceptor requestInterceptor) {
        return new RestAdapter.Builder().setRequestInterceptor(requestInterceptor).setEndpoint(BuildConfig.UBER_ENDPOINT).setLogLevel(
                RestAdapter.LogLevel.FULL).build();
    }

    @Provides
    @Singleton
    UberAPI provideUberAPI(@Named("UberAPIAdapter") RestAdapter restAdapter) {
        return restAdapter.create(UberAPI.class);
    }

    @Provides
    @Singleton
    UberRidesPresenter provideUberRidesPresenter(FusedLocationManager fusedLocationManager, UberAPI uberAPI) {
        return new UberRidesPresenterImpl(fusedLocationManager, uberAPI);
    }

    @Provides
    @Singleton
    MainPresenter provideMainPresenter(FusedLocationManager fusedLocationManager, UberAPI uberAPI) {
        return new MainPresenterImpl();
    }
}
