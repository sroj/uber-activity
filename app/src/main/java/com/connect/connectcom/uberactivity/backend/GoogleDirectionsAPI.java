package com.connect.connectcom.uberactivity.backend;

import com.connect.connectcom.uberactivity.backend.model.DirectionsGETResponse;

import retrofit.http.GET;
import retrofit.http.Query;
import rx.Observable;

/**
 * Created by Simon on 8/27/2015.
 */
public interface GoogleDirectionsAPI {


    @GET("/maps/api/directions/json")
    Observable<DirectionsGETResponse> getDirections(@Query("origin") String origin,
                                                    @Query("destination") String destination);
}
