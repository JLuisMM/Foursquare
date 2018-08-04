package com.example.luis.codingchallenge.manager.events;

import com.example.luis.codingchallenge.model.Venue;

public class ShowVenueDetailEvent {

    Venue mVenue;

    public ShowVenueDetailEvent(Venue mVenue) {
        this.mVenue = mVenue;
    }

    public Venue getVenue() {
        return mVenue;
    }
}
