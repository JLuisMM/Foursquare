package com.example.luis.codingchallenge.ui.mvp.placesearch;

import android.location.Location;

import com.example.luis.codingchallenge.model.ResponseVenues;
import com.example.luis.codingchallenge.ui.mvp.BasePresenter;
import com.example.luis.codingchallenge.ui.mvp.BaseView;

public interface PlaceSearchContact {

    interface View extends BaseView {

        void showSearchResult(String title, ResponseVenues response);

        void showPlaceSearchingPopup();

        void showWarnUserInput(String warnText);

        void showTypeLocationForcelyPopup();

        void showNeedGpsPopup();

        void showApiFailError();

    }

    interface Presenter<V extends BaseView> extends BasePresenter<View> {

        void searchPlace(String type, String where, Location location);
    }


}
