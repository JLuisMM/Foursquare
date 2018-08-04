package com.example.luis.codingchallenge.ui.fragments;


import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.afollestad.materialdialogs.MaterialDialog;
import com.example.luis.codingchallenge.R;
import com.example.luis.codingchallenge.manager.events.RefreshLocationEvent;
import com.example.luis.codingchallenge.manager.events.ShowVenuesListEvent;
import com.example.luis.codingchallenge.model.ResponseVenues;
import com.example.luis.codingchallenge.ui.mvp.placesearch.PlaceSearchContact;
import com.example.luis.codingchallenge.ui.mvp.placesearch.PlaceSearchPresenter;
import com.example.luis.codingchallenge.utility.LocationUtility;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class PlaceSearchFragment extends Fragment implements PlaceSearchContact.View {

    public static final String CURRENT_LOCATION = "current_location";

    PlaceSearchPresenter mPresenter;

    @BindView(R.id.edit_text_place_search)
    EditText mPlaceTypeEditText;

    @BindView(R.id.button_place_search)
    Button mSearchButton;

    private Location mCurrentLocation;

    MaterialDialog mProgressbar;


    public static PlaceSearchFragment newInstance(Location mCurrentLocation) {
        if (mCurrentLocation == null) {
            return new PlaceSearchFragment();
        }

        PlaceSearchFragment placeSearchFragment = new PlaceSearchFragment();
        Bundle mBundle = new Bundle();
        mBundle.putParcelable(CURRENT_LOCATION, mCurrentLocation);
        placeSearchFragment.setArguments(mBundle);

        return placeSearchFragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        EventBus.getDefault().register(this);
    }

    @Override
    public void onDetach() {

        EventBus.getDefault().unregister(this);
        super.onDetach();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null)
            mCurrentLocation = getArguments().getParcelable(CURRENT_LOCATION);

        //create our presenter
        if (mPresenter == null) {
            mPresenter = new PlaceSearchPresenter();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_place_search, container, false);
        ButterKnife.bind(this, view);

        mPresenter.attachView(this);

        return view;
    }

    @Override
    public void showSearchResult(String title, ResponseVenues response) {
        hideLoading();
        EventBus.getDefault().post(new ShowVenuesListEvent(response, title));
    }

    @Override
    public void showPlaceSearchingPopup() {
        mProgressbar = new MaterialDialog.Builder(getContext())
                .progress(true, 0).content(R.string.searching).cancelable(false)
                .show();
    }

    @Override
    public void showWarnUserInput(String warnText) {
        new MaterialDialog.Builder(getContext())
                .title(R.string.popup_invalid_input_title)
                .content(R.string.popup_content_invalid_input)
                .positiveText(R.string.ok).show();
    }

    @Override
    public void showTypeLocationForcelyPopup() {
        new MaterialDialog.Builder(getContext()).title(R.string.popup_invalid_input_title)
                .content(R.string.popup_content_fail_location).show();
    }

    @Override
    public void showNeedGpsPopup() {
        openGpsPopup();
    }

    @Override
    public void showApiFailError() {
        new MaterialDialog.Builder(getContext())
                .title(R.string.popup_search_fail_title)
                .content(R.string.popup_content_search_fail)
                .positiveText(R.string.ok).show();
    }

    @OnClick({R.id.button_place_search})
    public void onClick(View view) {
        if (view.getId() == R.id.button_place_search) {
            String placeType = mPlaceTypeEditText.getText().toString();
            String where = "seattle";

            mPresenter.searchPlace(placeType, where, mCurrentLocation);
        }
    }

    private void openGpsPopup() {
        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivity(intent);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(RefreshLocationEvent event) {

        refreshLocation();
    }

    private void refreshLocation() {
        mCurrentLocation = LocationUtility.getCurrentLocation((LocationManager)
                (getActivity().getSystemService(Context.LOCATION_SERVICE)));
    }


    private void hideLoading() {
        if (mProgressbar != null)
            mProgressbar.dismiss();
    }

    @Override
    public void onDestroyView() {
        mPresenter.detachView();
        super.onDestroyView();
    }
}