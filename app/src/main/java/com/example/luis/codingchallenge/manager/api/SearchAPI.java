package com.example.luis.codingchallenge.manager.api;

import com.example.luis.codingchallenge.model.VenueDetailResponse;
import com.example.luis.codingchallenge.model.VenuesListResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface SearchAPI {

    @GET("/v2/venues/search")
    Call<VenuesListResponse> getVenues(@Query("v") String version, @Query("client_id") String clientID, @Query("client_secret") String clientSecret, @Query("query") String placeType, @Query("near") String near);

    @GET("/v2/venues/search")
    Call<VenuesListResponse> getVenuesByLocation(@Query("v") String version, @Query("client_id") String clientID, @Query("client_secret") String clientSecret, @Query("query") String placeType, @Query("ll") String latLng);

    @GET("/v2/venues/{venue_id}")
    Call<VenueDetailResponse> getVenueDetail(@Path("venue_id") String venueID, @Query("v") String version, @Query("client_id") String clientID, @Query("client_secret") String clientSecret);
}
