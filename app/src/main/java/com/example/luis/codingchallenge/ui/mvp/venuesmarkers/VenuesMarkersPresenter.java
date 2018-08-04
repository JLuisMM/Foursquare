package com.example.luis.codingchallenge.ui.mvp.venuesmarkers;

import com.example.luis.codingchallenge.manager.events.ShowVenueDetailEvent;
import com.example.luis.codingchallenge.model.ResponseVenues;
import com.example.luis.codingchallenge.model.Venue;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.greenrobot.eventbus.EventBus;

public class VenuesMarkersPresenter implements VenuesMarkersInterface.Presenter<VenuesMarkersInterface.View> {

    private VenuesMarkersInterface.View mView;

    @Override
    public void attachView(VenuesMarkersInterface.View view) {
        mView = view;
    }

    @Override
    public void detachView() {
        mView = null;
    }

    public GoogleMap setMarkers(GoogleMap googleMap, final ResponseVenues response) {
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        LatLng latLng;
        Venue venue;
        for (int i=0 ; i < response.getVenues().size() ; i++) {
            venue = response.getVenue(i);
            latLng = new LatLng(venue.getLocation().getLat(), venue.getLocation().getLng());
            builder.include(latLng);
            googleMap.addMarker(new MarkerOptions()
                    .position(latLng)
                    .anchor(0.5f, 0.5f)
                    .title(venue.getName())
                    .snippet(venue.getUrl())
                    .zIndex(i));
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
        }

        googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                marker.getId();
                Venue selectedVenue = response.getVenue(Math.round(marker.getZIndex()));
                EventBus.getDefault().post(new ShowVenueDetailEvent(selectedVenue));
            }
        });

        return googleMap;
    }
}
