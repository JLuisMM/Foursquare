package com.example.luis.codingchallenge.manager.events;

import com.example.luis.codingchallenge.model.ResponseVenues;

public class ShowVenuesMarkersEvent {
    private ResponseVenues mResponse;

    public ShowVenuesMarkersEvent(ResponseVenues mResponse) {
        this.mResponse = mResponse;
    }

    public ResponseVenues getResponse() {
        return mResponse;
    }
}
