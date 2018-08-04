package com.example.luis.codingchallenge.ui.mvp.venuesmarkers;

import com.example.luis.codingchallenge.ui.mvp.BasePresenter;
import com.example.luis.codingchallenge.ui.mvp.BaseView;

public interface VenuesMarkersInterface {

    interface View extends BaseView {
    }

    interface Presenter<V extends BaseView> extends BasePresenter<VenuesMarkersInterface.View> {
    }
}
