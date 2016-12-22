package com.stroeer.configurationtestapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import de.stroeer.configurationsdk.ConfigurationApi;
import de.stroeer.configurationsdk.model.BeaconWithBattery;

public class MainActivity extends AppCompatActivity implements ConfigurationApi.Listener {

    private static final String API_KEY = "Type Api-key here";

    private RequirementsChecker requirementsChecker;

    private TextView tvLog;
    private Handler mainThreadHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.tvLog = (TextView) findViewById(R.id.tvLog);

        this.requirementsChecker = new RequirementsChecker(this);
        this.mainThreadHandler = new Handler();

        ConfigurationApi.getInstance(this).registerConfigurationListener(this);
        ConfigurationApi.getInstance(this).setApiKey(API_KEY);
    }

    @Override
    public void onBeaconDataChanged(final BeaconWithBattery beaconWithBattery) {
        this.mainThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                tvLog.setText(tvLog.getText() + generateBeaconText(beaconWithBattery));
            }
        });
    }

    private String generateBeaconText(final BeaconWithBattery beaconWithBattery) {
        StringBuffer sb = new StringBuffer();
        sb.append(beaconWithBattery.getMajor());
        sb.append(":");
        sb.append(beaconWithBattery.getMinor());
        sb.append(" = ");
        sb.append(beaconWithBattery.getChargeState());
        sb.append(" sent to server: ");
        sb.append(beaconWithBattery.sentToServer());
        sb.append("\n");
        return sb.toString();
    }

    @Override
    public void onBeaconDataCleared() {
        this.mainThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                tvLog.setText("");
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        ConfigurationApi.getInstance(this).unregisterConfigurationListener(this);
    }

    @Override
    public void onFailure(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.requirementsChecker.checkRequirements();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        this.requirementsChecker.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        onActivityResult(requestCode, resultCode, data);
        this.requirementsChecker.onActivityResult(requestCode, resultCode, data);
    }
}
