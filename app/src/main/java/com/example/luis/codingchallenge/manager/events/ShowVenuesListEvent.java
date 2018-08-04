package com.example.luis.codingchallenge.manager.events;

import com.example.luis.codingchallenge.model.ResponseVenues;

public class ShowVenuesListEvent {
    private String title;
    private ResponseVenues mResponse;

    public ShowVenuesListEvent(ResponseVenues mResponse,String title) {
        this.mResponse = mResponse;
        this.title=title;
    }

    public ResponseVenues getResponse() {
        return mResponse;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}