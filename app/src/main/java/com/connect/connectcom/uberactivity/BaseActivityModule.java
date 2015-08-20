package com.connect.connectcom.uberactivity;

import com.connect.connectcom.uberactivity.presenter.UberRidesPresenter;
import com.connect.connectcom.uberactivity.presenter.UberRidesPresenterImpl;

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
        injects = {
                BaseActivity.class,
                UberRideFragment.class,
                UberRidesPresenterImpl.class
        },
        addsTo = BaseApplicationModule.class,
        library = true
)
public final class BaseActivityModule {

    @Provides
    @Singleton
    @Named("UberRequestInterceptor")
    RequestInterceptor provideUberRequestInterceptor() {
        return new RequestInterceptor() {
            @Override
            public void intercept(RequestInterceptor.RequestFacade request) {
                request.addHeader(
                        Constants.AUTHORIZATION_HEADER_NAME,
                        BuildConfig.UBER_SERVER_KEY
                );
            }
        };
    }

    @Provides
    @Singleton
    @Named("UberAPIAdapter")
    RestAdapter provideUberAPIAdapter(
            @Named("UberRequestInterceptor") RequestInterceptor requestInterceptor
    ) {
        return new RestAdapter.Builder().setRequestInterceptor(requestInterceptor)
                .setEndpoint(BuildConfig.UBER_ENDPOINT)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();
    }

    @Provides
    @Singleton
    UberAPI provideUberAPI(@Named("UberAPIAdapter") RestAdapter restAdapter) {
        return restAdapter.create(UberAPI.class);
    }

    @Provides
    @Singleton
    UberRidesPresenter provideUberRidesPresenter(FusedLocationManager fusedLocationManager,
                                                 UberAPI uberAPI) {
        return new UberRidesPresenterImpl(fusedLocationManager, uberAPI);
    }
}
