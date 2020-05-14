package com.abhibhr.cryptorate.main;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.abhibhr.cryptorate.location.LocationManager;

public class LocationActivity extends TrackerActivity {

    public static final String TAG = LocationActivity.class.getSimpleName();
    public static final int PERMISSION_REQUEST_LOCATION = 1234;
    private LocationManager mLocationManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLocationManager = new LocationManager(this, mTracker);
        checkLocationPermission();
        getLifecycle().addObserver(mLocationManager);
    }


    private void checkLocationPermission(){
        Log.d(TAG, "checkLocatioParmission() called");
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                Toast.makeText(this, "Please give location permission", Toast.LENGTH_LONG).show();

            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_LOCATION);
            }
        } else mLocationManager.buildGoogleApiClient();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d(TAG, "onRequestPermissionResult() called with; requestCode = ["+ requestCode + "], permissions = [" +
                permissions + "], grantresults = [" +grantResults+"]");

        switch (requestCode) {
            case PERMISSION_REQUEST_LOCATION: {
                if (grantResults.length >0 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        mLocationManager.buildGoogleApiClient();
                    }
                }else {
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            PERMISSION_REQUEST_LOCATION);
                }
                return;
            }
        }
    }
}


