package com.example.luis.codingchallenge.manager.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;

import com.example.luis.codingchallenge.manager.events.RefreshLocationEvent;

import org.greenrobot.eventbus.EventBus;

import java.util.Objects;

public class LocationProviderReceiver extends BroadcastReceiver {

    public static final String LOCATION_SETTING_CHANGED = "android.location.PROVIDERS_CHANGED";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (Objects.requireNonNull(intent.getAction()).matches(LOCATION_SETTING_CHANGED)) {
            LocationManager manager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            if (Objects.requireNonNull(manager).isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                EventBus.getDefault().post(new RefreshLocationEvent());
            }
        }
    }
}
