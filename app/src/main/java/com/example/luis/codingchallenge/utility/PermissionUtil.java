package com.example.luis.codingchallenge.utility;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;

public class PermissionUtil {

    public static boolean hasPermission(AppCompatActivity mActivity, String permisson) {
        return ActivityCompat.checkSelfPermission(mActivity, permisson) != PackageManager.PERMISSION_GRANTED;
    }

    public static boolean hasAccessFineLocationPermission(AppCompatActivity mActivity) {
        return hasPermission(mActivity, Manifest.permission.ACCESS_FINE_LOCATION);
    }
}
