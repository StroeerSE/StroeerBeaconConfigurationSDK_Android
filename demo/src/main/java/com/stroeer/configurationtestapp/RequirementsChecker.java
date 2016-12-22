package com.stroeer.configurationtestapp;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;


import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.support.v4.content.PermissionChecker.PERMISSION_GRANTED;

/**
 * Created by dustin on 13.12.16.
 */
public class RequirementsChecker {

    private static final int REQUEST_LOCATION_PERMISSION_START_CONFIG = 1;
    private static final int REQUEST_ENABLE_BT_START_CONFIG = 2;


    private BluetoothManager mBtManager;
    private BluetoothAdapter mBtAdapter;

    private AppCompatActivity activity;

    public RequirementsChecker(final AppCompatActivity activity) {
        this.activity = activity;

        this.mBtManager = (BluetoothManager) this.activity.getSystemService(Context.BLUETOOTH_SERVICE);
        this.mBtAdapter = this.mBtManager.getAdapter();
    }


    public void checkRequirements() {
        new Handler().post(new Runnable() {
            public void run() {
                boolean granted = mBtAdapter.isEnabled();
                if (!granted) {
                    activity.startActivityForResult(new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE), REQUEST_ENABLE_BT_START_CONFIG);
                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        granted = ActivityCompat.checkSelfPermission(activity, ACCESS_COARSE_LOCATION) == PERMISSION_GRANTED;
                        if (!granted) {
                            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_LOCATION_PERMISSION_START_CONFIG);
                        } else {
                            granted = isLocationServicesOn(activity);
                            if (!granted) {
                                new LocationSettingsDialog().show(activity.getSupportFragmentManager(), LocationSettingsDialog.class.getSimpleName());
                            }
                        }
                    }
                }
            }
        });
    }

    private static boolean isLocationServicesOn(Context context) {
        try {
            return Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE) != Settings.Secure.LOCATION_MODE_OFF;
        } catch (Settings.SettingNotFoundException e) {
            return false;
        }
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_LOCATION_PERMISSION_START_CONFIG:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    checkRequirements();
                } else {
                    Toast.makeText(this.activity, R.string.warning_permission_missing_location, Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_ENABLE_BT_START_CONFIG:
                if (resultCode == Activity.RESULT_OK) {
                    checkRequirements();
                } else {
                    Toast.makeText(this.activity, R.string.warning_bluetooth_disabled, Toast.LENGTH_LONG).show();
                }
                break;
        }
    }
}
