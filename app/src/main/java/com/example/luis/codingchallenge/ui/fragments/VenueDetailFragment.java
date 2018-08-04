package com.example.luis.codingchallenge.ui.fragments;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.luis.codingchallenge.manager.Interactor;
import com.example.luis.codingchallenge.R;
import com.example.luis.codingchallenge.adapters.UserCommentAdapter;
import com.example.luis.codingchallenge.manager.api.SearchAPI;
import com.example.luis.codingchallenge.model.Location;
import com.example.luis.codingchallenge.model.Photo;
import com.example.luis.codingchallenge.model.ResponseVenue;
import com.example.luis.codingchallenge.model.TipGroup;
import com.example.luis.codingchallenge.model.Tips;
import com.example.luis.codingchallenge.model.UserComment;
import com.example.luis.codingchallenge.model.Venue;
import com.example.luis.codingchallenge.model.VenueDetailResponse;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class VenueDetailFragment extends DialogFragment implements OnMapReadyCallback {

    public static final String TAG = "VenueDetailFragment";
    public static final String VENUE_OBJ = "venue_obj";
    public static final String FOURSQUORE_VERSION = "20180323";
    public static final String FOURSQUORE_CLIENT_ID = "YJPVTRLAUPJW2BCTRV2YOHKBI0LZEOZIXHFQIFNITX2JKHA1";
    public static final String FOURSQUORE_SECRET_ID = "0XOOFVSLGAIX1RLON2COG0L0ZBG5BDY0ORE53ZQQGNICIEG1";
    public static final String BASE_URL = "https://api.foursquare.com";

    Venue mVenue;

    @BindView(R.id.recyclerview_user_comments)
    RecyclerView mCommentRecyclerView;

    @BindView(R.id.image_venue_detail_venue_icon)
    ImageView mVenueIcon;

    @BindView(R.id.card_venue_detail_venue_name)
    TextView mVenueName;

    @BindView(R.id.card_venue_detail_venue_url)
    TextView mVenueUrl;

    @BindView(R.id.map_venue_detail)
    MapView mMap;

    private UserCommentAdapter mUserCommentAdapter;


    public static VenueDetailFragment NewInstance(Venue mVenue) {

        VenueDetailFragment venueDetailFragment = new VenueDetailFragment();

        Bundle mBundle = new Bundle();
        mBundle.putParcelable(VENUE_OBJ, mVenue);

        venueDetailFragment.setArguments(mBundle);

        return venueDetailFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.VenueDialogStyle);

        if (getArguments() != null) {
            mVenue = getArguments().getParcelable(VENUE_OBJ);
        }
    }

    public static final ButterKnife.Setter<View, Integer> VISIBILITY = new ButterKnife.Setter<View, Integer>() {
        @Override
        public void set(@NonNull View view, Integer value, int index) {
            view.setVisibility(value);
        }
    };

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
        super.onDestroy();
        mMap.onDestroy();
    }

    private void getVenueDetail() {
        Retrofit mRetrofit = Interactor.restInteractor().retrofit();

        SearchAPI searchAPI = mRetrofit.create(SearchAPI.class);

        searchAPI.getVenueDetail(mVenue.getId(), FOURSQUORE_VERSION,
                FOURSQUORE_CLIENT_ID, FOURSQUORE_SECRET_ID)
                .enqueue(new Callback<VenueDetailResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<VenueDetailResponse> call,
                                           @NonNull Response<VenueDetailResponse> response) {
                        VenueDetailResponse venueDetailResponse = response.body();

                        if (venueDetailResponse != null) {
                            ResponseVenue responseVenue = venueDetailResponse.getResponse();
                            Venue venue = responseVenue.getVenue();

                            showPlaceImage(venue.getBestPhoto());

                            Tips tips = venue.getTips();
                            ArrayList<TipGroup> groups = tips.getGroups();

                            ArrayList<UserComment> userComments = groups.get(0).getItems();
                            refreshComments(userComments);
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<VenueDetailResponse> call, @NonNull Throwable t) {
                        //no-op
                    }
                });

    }

    private void refreshComments(ArrayList<UserComment> comments) {

        mUserCommentAdapter.setUserComments(comments);
        mUserCommentAdapter.notifyItemChanged(comments.size());
    }

    private void showPlaceImage(Photo photo) {
        if (photo == null) {
            return;
        }

        String photoUrl = photo.getImageBySize("400x400");
        if (photoUrl == null) {
            return;
        }

        Picasso.with(getContext()).load(photoUrl).into(mVenueIcon);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location location = mVenue.getLocation();
        googleMap.setMyLocationEnabled(true);
        googleMap.addMarker(new MarkerOptions().position(new LatLng(47.606200, -122.332100)).title("Center"));
        googleMap.addMarker(new MarkerOptions().position(new LatLng(location.getLat(), location.getLng())).title(mVenue.getName()));
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLat(), location.getLng()), 14));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_venue_detail, container, false);
        ButterKnife.bind(this, view);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        mCommentRecyclerView.setLayoutManager(linearLayoutManager);

        mUserCommentAdapter = new UserCommentAdapter(null);
        mCommentRecyclerView.setAdapter(mUserCommentAdapter);

        mVenueName.setText(mVenue.getName());

        if (mVenue.getUrl() != null) {
            mVenueUrl.setText(mVenue.getUrl());
        } else {
            ButterKnife.apply(mVenueUrl, VISIBILITY, View.GONE);
        }

        getVenueDetail();

        MapsInitializer.initialize(getContext());
        mMap.onCreate(savedInstanceState);
        mMap.getMapAsync(this);

        return view;
    }

    @OnClick({R.id.card_venue_detail_venue_url})
    public void onClick(View view) {
        if (view.getId() == R.id.card_venue_detail_venue_url) {
            openWebPage(mVenue.getUrl());
        }
    }

    private void openWebPage(String url) {
        try {
            Uri webpage = Uri.parse(url);
            Intent myIntent = new Intent(Intent.ACTION_VIEW, webpage);
            startActivity(myIntent);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }
}
