package com.connect.connectcom.uberactivity.presenter;

import android.location.Location;

import com.connect.connectcom.uberactivity.backend.ConnectivityStatusManager;
import com.connect.connectcom.uberactivity.backend.UberAPI;
import com.connect.connectcom.uberactivity.location.FusedLocationManager;
import com.connect.connectcom.uberactivity.model.Price;
import com.connect.connectcom.uberactivity.model.Prices;
import com.connect.connectcom.uberactivity.model.Product;
import com.connect.connectcom.uberactivity.model.Products;
import com.connect.connectcom.uberactivity.model.Time;
import com.connect.connectcom.uberactivity.model.Times;
import com.connect.connectcom.uberactivity.model.UberRide;
import com.connect.connectcom.uberactivity.util.Ln;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.functions.Func3;

/**
 * Created by Simon on 8/18/2015.
 */
public class UberRidesPresenterImpl extends UberRidesPresenter {

    private FusedLocationManager fusedLocationManager;
    private UberAPI uberAPI;

    private Subscription fetchUberRidesSubscription;

    public UberRidesPresenterImpl(FusedLocationManager fusedLocationManager, UberAPI uberApi) {
        this.fusedLocationManager = fusedLocationManager;
        this.uberAPI = uberApi;
    }

    @Override
    public void onRideSelected(UberRide uberRide) {
        getView().launchUberActivity(uberRide);
    }

    @Override
    public void loadUberRides(final double dropOffLatitude, final double dropOffLongitude) {

        getView().showProgress();

        if (fetchUberRidesSubscription != null && !fetchUberRidesSubscription.isUnsubscribed()) {
            fetchUberRidesSubscription.unsubscribe();
            fetchUberRidesSubscription = null;
        }

        fetchUberRidesSubscription = fusedLocationManager.getLastKnownLocationObservable().observeOn(AndroidSchedulers.mainThread()).flatMap(
                new Func1<Location, Observable<List<UberRide>>>() {
                    @Override
                    public Observable<List<UberRide>> call(final Location location) {
                        if (location != null) {

                            // ****************************
                            // FOR DEBUGGING IN VENEZUELA - No UBER here!! :(
                            // ****************************
                            location.setLatitude(40.740464);
                            location.setLongitude(-73.948749);

                            getView().drawPickupMarker(location.getLatitude(), location.getLongitude());
                            getView().drawDropOffMarker(dropOffLatitude, dropOffLongitude);

                            Observable<Products> fetchProducts = uberAPI.getProducts(location.getLatitude(),
                                                                                     location.getLongitude()).doOnError(
                                    new Action1<Throwable>() {
                                        @Override
                                        public void call(final Throwable e) {
                                            Ln.e(e);
                                        }
                                    }).onErrorReturn(new Func1<Throwable, Products>() {
                                @Override
                                public Products call(Throwable throwable) {
                                    return Products.buildErrorObject();
                                }
                            });

                            Observable<Prices> fetchPrices = uberAPI.getPrices(location.getLatitude(),
                                                                               location.getLongitude(),
                                                                               dropOffLatitude,
                                                                               dropOffLongitude).doOnError(new Action1<Throwable>() {
                                @Override
                                public void call(final Throwable e) {
                                    Ln.e(e);
                                }
                            }).onErrorReturn(new Func1<Throwable, Prices>() {
                                @Override
                                public Prices call(Throwable throwable) {
                                    return Prices.buildErrorObject();
                                }
                            });

                            Observable<Times> fetchTimes = uberAPI.getTimes(location.getLatitude(),
                                                                            location.getLongitude()).doOnError(new Action1<Throwable>() {
                                @Override
                                public void call(final Throwable e) {
                                    Ln.e(e);
                                }
                            }).onErrorReturn(new Func1<Throwable, Times>() {
                                @Override
                                public Times call(Throwable throwable) {
                                    return Times.buildErrorObject();
                                }
                            });

                            return Observable.zip(fetchProducts,
                                                  fetchPrices,
                                                  fetchTimes,
                                                  new Func3<Products, Prices, Times, List<UberRide>>() {
                                                      @Override
                                                      public List<UberRide> call(Products products,
                                                                                 Prices prices,
                                                                                 Times times) {
                                                          if (products.isOk() && times.isOk() && prices.isOk()) {
                                                              List<UberRide> uberRides = new ArrayList<>();
                                                              int numProducts = products.getProducts().size();
                                                              int numPrices = prices.getPrices().size();
                                                              int numTimes = times.getTimes().size();

                                                              // Not going to do a for loop for this really limited dataset...
                                                              int min = Math.min(Math.min(numProducts, numPrices),
                                                                                 numTimes);

                                                              if (!((numProducts == numPrices) && numPrices == numTimes)) {
                                                                  Ln.w("HEY! The Uber API responses had different number of elements");
                                                              }
                                                              List<Product> productList = products.getProducts();
                                                              List<Price> priceList = prices.getPrices();
                                                              List<Time> timeList = times.getTimes();

                                                              for (int i = 0; i < min; i++) {
                                                                  UberRide uberRide = new UberRide();
                                                                  uberRide.setProduct(productList.get(i));
                                                                  uberRide.setPrice(priceList.get(i));
                                                                  uberRide.setTime(timeList.get(i));
                                                                  uberRide.setPickupLat(location.getLatitude());
                                                                  uberRide.setPickupLon(location.getLongitude());
                                                                  uberRide.setDropOffLat(dropOffLatitude);
                                                                  uberRide.setDropOffLon(dropOffLongitude);
                                                                  uberRides.add(uberRide);
                                                              }
                                                              return uberRides;
                                                          } else {
                                                              return new ArrayList<>();
                                                          }
                                                      }
                                                  }).observeOn(AndroidSchedulers.mainThread());
                        } else {
                            return Observable.<List<UberRide>>just(new ArrayList<UberRide>());
                        }

                    }
                }).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<List<UberRide>>() {
            @Override
            public void onCompleted() {
                Ln.d("Initial location observable completed");
            }

            @Override
            public void onError(Throwable e) {
                Ln.e("Error while obtaining the initial network and location data: %s", e);
                // TODO The ConnectivityStatusManager is not plugged in right now
                if (e.getCause().getCause() instanceof ConnectivityStatusManager.NoConnectivityException) {
                    getView().showNoConnectivity();
                }
            }

            @Override
            public void onNext(List<UberRide> uberRides) {
                Ln.d("Initial location observable emitted this data items: %s", uberRides);

                if (getView() != null) {
                    if (!uberRides.isEmpty()) {
                        getView().showRides(uberRides);
                        final UberRide uberRide = uberRides.get(0);
                        getView().drawRoute(uberRide.getPickupLat(),
                                            uberRide.getPickupLon(),
                                            uberRide.getDropOffLat(),
                                            uberRide.getDropOffLon());

                    } else {
                        getView().showNoRides();
                    }
                }
            }
        });
    }

    @Override
    public void onMapReady(double dropOffLatitude, double dropOffLongitude) {
        Ln.d("Map is ready!");
        loadUberRides(dropOffLatitude, dropOffLongitude);
    }
}
