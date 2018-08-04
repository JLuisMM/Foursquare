package com.example.luis.codingchallenge.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class ResponseVenues implements Parcelable {

    private ArrayList<Venue> venues;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(this.venues);
    }

    public ResponseVenues() {
    }

    protected ResponseVenues(Parcel in) {
        this.venues = new ArrayList<Venue>();
        in.readList(this.venues, Venue.class.getClassLoader());
    }

    public static final Parcelable.Creator<ResponseVenues> CREATOR = new Parcelable.Creator<ResponseVenues>() {
        @Override
        public ResponseVenues createFromParcel(Parcel source) {
            return new ResponseVenues(source);
        }

        @Override
        public ResponseVenues[] newArray(int size) {
            return new ResponseVenues[size];
        }
    };

    public ArrayList<Venue> getVenues() {
        return venues;
    }

    public Venue getVenue(int pos) {

        if (venues == null)
            return null;

        return venues.get(pos);
    }
}
