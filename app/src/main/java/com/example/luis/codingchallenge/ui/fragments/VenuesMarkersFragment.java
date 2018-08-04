package com.example.luis.codingchallenge.ui.fragments;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.luis.codingchallenge.R;
import com.example.luis.codingchallenge.model.ResponseVenues;
import com.example.luis.codingchallenge.ui.mvp.venuesmarkers.VenuesMarkersInterface;
import com.example.luis.codingchallenge.ui.mvp.venuesmarkers.VenuesMarkersPresenter;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;

import butterknife.BindView;
import butterknife.ButterKnife;

public class VenuesMarkersFragment extends DialogFragment implements VenuesMarkersInterface.View, OnMapReadyCallback {

    public static final String RESPONSE_OBJ = "response_obj";

    VenuesMarkersPresenter mPresenter;

    ResponseVenues mResponse;

    @BindView(R.id.map_venues)
    MapView mMap;

    public static VenuesMarkersFragment NewInstance(ResponseVenues response) {

        VenuesMarkersFragment venuesMarkersFragment = new VenuesMarkersFragment();

        Bundle mBundle = new Bundle();
        mBundle.putParcelable(RESPONSE_OBJ, response);

        venuesMarkersFragment.setArguments(mBundle);
        return venuesMarkersFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.VenueDialogStyle);

        if (getArguments() != null) {
            mResponse = getArguments().getParcelable(RESPONSE_OBJ);
        }

        //create our presenter
        if (mPresenter == null) {
            mPresenter = new VenuesMarkersPresenter();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map_view, container, false);
        ButterKnife.bind(this, view);

        MapsInitializer.initialize(getContext());
        mMap.onCreate(savedInstanceState);
        mMap.getMapAsync(this);

        mPresenter.attachView(this);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mMap.onResume();
    }

    @Override
    public void onStop() {
        super.onStop();
        mMap.onStop();
    }

    @Override
    public void onDestroy() {
        mPresenter.detachView();
        super.onDestroy();
        mMap.onDestroy();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        if (ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        googleMap.setMyLocationEnabled(true);
        mPresenter.setMarkers(googleMap, mResponse);
    }
}
