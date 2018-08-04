package com.example.luis.codingchallenge.ui.mvp.placesearch;

import android.location.Location;
import android.support.annotation.NonNull;

import com.example.luis.codingchallenge.manager.Interactor;
import com.example.luis.codingchallenge.manager.api.SearchAPI;
import com.example.luis.codingchallenge.model.VenuesListResponse;
import com.example.luis.codingchallenge.ui.mvp.placesearch.PlaceSearchContact.Presenter;
import com.example.luis.codingchallenge.utility.StringValidator;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static com.example.luis.codingchallenge.ui.fragments.VenueDetailFragment.FOURSQUORE_CLIENT_ID;
import static com.example.luis.codingchallenge.ui.fragments.VenueDetailFragment.FOURSQUORE_SECRET_ID;
import static com.example.luis.codingchallenge.ui.fragments.VenueDetailFragment.FOURSQUORE_VERSION;

public class PlaceSearchPresenter implements Presenter<PlaceSearchContact.View> {

    private PlaceSearchContact.View mView;

    @Override
    public void attachView(PlaceSearchContact.View view) {
        mView = view;
    }

    @Override
    public void detachView() {
        mView = null;
    }

    @Override
    public void searchPlace(final String type, String where, Location location) {
        if (!StringValidator.isValidPlaceType(type)) {
            this.mView.showWarnUserInput(null);
            return;
        }

        if ((where == null || where.isEmpty()) && location == null) {
            this.mView.showTypeLocationForcelyPopup();
            return;
        }

        this.mView.showPlaceSearchingPopup();

        Retrofit mRetrofit = Interactor.restInteractor().retrofit();
        SearchAPI searchAPI = mRetrofit.create(SearchAPI.class);

        if (where != null && !where.isEmpty()) {
            searchAPI.getVenues(FOURSQUORE_VERSION, FOURSQUORE_CLIENT_ID, FOURSQUORE_SECRET_ID, type, where).enqueue(new Callback<VenuesListResponse>() {
                @Override
                public void onResponse(@NonNull Call<VenuesListResponse> call, @NonNull Response<VenuesListResponse> response) {

                    VenuesListResponse fourSquareResponse = response.body();
                    if (fourSquareResponse == null) {
                        mView.showSearchResult(type, null);
                        return;
                    }
                    mView.showSearchResult(type, fourSquareResponse.getResponse());
                }

                @Override
                public void onFailure(@NonNull Call<VenuesListResponse> call, @NonNull Throwable t) {
                    mView.showApiFailError();
                }
            });
            return;
        }

        String latLng = String.valueOf(location.getLatitude()) + "," + String.valueOf(location.getLongitude());
        searchAPI.getVenuesByLocation(FOURSQUORE_VERSION, FOURSQUORE_CLIENT_ID, FOURSQUORE_SECRET_ID, type, latLng).enqueue(new Callback<VenuesListResponse>() {
            @Override
            public void onResponse(@NonNull Call<VenuesListResponse> call, @NonNull Response<VenuesListResponse> response) {
                VenuesListResponse fourSquareResponse = response.body();
                mView.showSearchResult(type, Objects.requireNonNull(fourSquareResponse).getResponse());
            }

            @Override
            public void onFailure(@NonNull Call<VenuesListResponse> call, @NonNull Throwable t) {
                mView.showApiFailError();
            }
        });
    }
}