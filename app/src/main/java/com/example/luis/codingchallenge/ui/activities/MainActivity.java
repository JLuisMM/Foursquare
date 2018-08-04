package com.example.luis.codingchallenge.ui.activities;

import android.content.Context;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;

import com.example.luis.codingchallenge.R;
import com.example.luis.codingchallenge.manager.events.RefreshLocationEvent;
import com.example.luis.codingchallenge.manager.events.ShowVenueDetailEvent;
import com.example.luis.codingchallenge.manager.events.ShowVenuesListEvent;
import com.example.luis.codingchallenge.manager.events.ShowVenuesMarkersEvent;
import com.example.luis.codingchallenge.model.ResponseVenues;
import com.example.luis.codingchallenge.model.Venue;
import com.example.luis.codingchallenge.ui.fragments.PlaceListFragment;
import com.example.luis.codingchallenge.ui.fragments.PlaceSearchFragment;
import com.example.luis.codingchallenge.ui.fragments.VenueDetailFragment;
import com.example.luis.codingchallenge.ui.fragments.VenuesMarkersFragment;
import com.example.luis.codingchallenge.utility.LocationUtility;
import com.example.luis.codingchallenge.utility.PermissionUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.INTERNET;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.activity_header)
    FrameLayout mHeader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EventBus.getDefault().register(this);

        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        android.location.Location location = LocationUtility.getCurrentLocation((LocationManager) getSystemService(Context.LOCATION_SERVICE));

        PlaceSearchFragment placeSearchFragment = PlaceSearchFragment.newInstance(location);

        getSupportFragmentManager().beginTransaction().add(R.id.activity_header, placeSearchFragment).commit();

        if (!hasAllPermissionsCheck()) {
            requestNeedPermissions();
        }
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
            Animation slideUp = AnimationUtils.loadAnimation(this, R.anim.slide_up);
            mHeader.startAnimation(slideUp);
            mHeader.setVisibility(View.VISIBLE);
            return;
        }
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        int menuID = menuItem.getItemId();

        if (menuID == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(menuItem);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        EventBus.getDefault().post(new RefreshLocationEvent());
    }

    private void requestNeedPermissions() {
        ActivityCompat.requestPermissions(this, new String[]{INTERNET, ACCESS_COARSE_LOCATION, ACCESS_FINE_LOCATION}, 1);
    }

    private boolean hasAllPermissionsCheck() {
        return checkPermissionGranted(INTERNET)
                && checkPermissionGranted(ACCESS_FINE_LOCATION)
                && checkPermissionGranted(ACCESS_COARSE_LOCATION);
    }

    private boolean checkPermissionGranted(String permission) {
        return PermissionUtil.hasPermission(this, permission);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void event(ShowVenuesMarkersEvent event) {
        if (event == null) {
            return;
        }
        ResponseVenues mResponse = event.getResponse();
        VenuesMarkersFragment venuesMarkersFragment = VenuesMarkersFragment.NewInstance(mResponse);
        venuesMarkersFragment.show(getSupportFragmentManager(), VenueDetailFragment.TAG);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void event(ShowVenuesListEvent event) {
        if (event == null) {
            return;
        }
        ResponseVenues mResponse = event.getResponse();
        getSupportFragmentManager().beginTransaction().add(R.id.activity_content, PlaceListFragment.NewInstance(mResponse)).addToBackStack(null).commit();
        Animation slideDown = AnimationUtils.loadAnimation(this, R.anim.slide_down);
        mHeader.startAnimation(slideDown);
        mHeader.setVisibility(View.GONE);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void event(ShowVenueDetailEvent event) {
        if (event == null) {
            return;
        }
        Venue mVenue = event.getVenue();
        VenueDetailFragment venueDetailFragment = VenueDetailFragment.NewInstance(mVenue);
        venueDetailFragment.show(getSupportFragmentManager(), VenueDetailFragment.TAG);
    }
}
