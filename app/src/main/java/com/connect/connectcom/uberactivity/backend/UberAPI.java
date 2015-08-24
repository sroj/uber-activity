package com.connect.connectcom.uberactivity.backend;


import com.connect.connectcom.uberactivity.model.Prices;
import com.connect.connectcom.uberactivity.model.Products;
import com.connect.connectcom.uberactivity.model.Times;

import retrofit.http.GET;
import retrofit.http.Query;
import rx.Observable;

/**
 * Created by Simon on 5/18/2015.
 */
public interface UberAPI {

    @GET("/v1/products")
    Observable<Products> getProducts(@Query("latitude") double latitude, @Query("longitude") double longitude);

    @GET("/v1/estimates/price")
    Observable<Prices> getPrices(@Query("start_latitude") double start_latitude,
                                 @Query("start_longitude") double start_longitude,
                                 @Query("end_latitude") double end_latitude,
                                 @Query("end_longitude") double end_longitude);

    @GET("/v1/estimates/time")
    Observable<Times> getTimes(@Query("start_latitude") double start_latitude,
                               @Query("start_longitude") double start_longitude);
}
