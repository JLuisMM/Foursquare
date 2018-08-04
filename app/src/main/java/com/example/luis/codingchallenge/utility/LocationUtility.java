package com.example.luis.codingchallenge.utility;

import android.app.Activity;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;

import java.util.List;

public class LocationUtility {

    public static Location getCurrentLocation(LocationManager mLocationManager) {

        if (mLocationManager == null)
            return null;

        List<String> providers = mLocationManager.getProviders(true);
        Location mCurrentLocation = null;


        for (String mProvider : providers) {

            try {

                mCurrentLocation = mLocationManager.getLastKnownLocation(mProvider);
                if (mCurrentLocation != null)
                    break;

            } catch (SecurityException ex) {

            }
        }

        return mCurrentLocation;
    }

}
