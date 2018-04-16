import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

import de.stroeer.configurationsdk.ConfigurationApi;
import de.stroeer.configurationsdk.model.BeaconWithBattery;


public class MainActivity extends AppCompatActivity implements ConfigurationApi.Listener {

    private static final String TAG = MainActivity.class.getSimpleName();

    private static final String API_KEY = "Type api-key here";

    private RequirementsChecker requirementsChecker;

    private TextView tvLog;

    private Map<String, BeaconWithBattery> beacons = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_main);
        this.tvLog = (TextView) findViewById(R.id.tvLog);

        this.requirementsChecker = new RequirementsChecker(this) {
            @Override
            public void onAllRequirementsSuccessful() {
                ConfigurationApi.getInstance(MainActivity.this).startScan();
            }
        };

        ConfigurationApi.getInstance(this).setStandStillDetectionDuration(15);
        ConfigurationApi.getInstance(this).setApiKey(API_KEY);
    }

    @Override
    protected void onResume() {
        super.onResume();
        ConfigurationApi.getInstance(this).registerConfigurationListener(this);
        this.requirementsChecker.checkRequirements();
    }

    @Override
    protected void onPause() {
        super.onPause();
        ConfigurationApi.getInstance(this).unregisterConfigurationListener(this);
    }


    @Override
    public void onBeaconDataChanged(final BeaconWithBattery beaconWithBattery) {
        this.beacons.put(beaconWithBattery.getMajor() + ":" + beaconWithBattery.getMinor(), beaconWithBattery);
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tvLog.setText(generateBeaconText());
            }
        });
    }

    @Override
    public void onBeaconDataCleared() {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tvLog.setText("");
            }
        });
    }

    @Override
    public void onFailure(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBeaconScanned(BeaconWithBattery beacon) {
        beacons.put(beacon.getMajor() + ":" + beacon.getMinor(), beacon);
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tvLog.setText(MainActivity.this.generateBeaconText());
            }
        });
    }

    @Override
    public void onScanStatusChanged(ConfigurationApi.ScanStatusCode newStatus) {
        String message = "";
        switch (newStatus) {
            case SCAN_STARTED:
                message = "Scan started";
                break;
            case SCAN_STOPPED:
                message = "Scan stopped";
                break;
            case SCAN_RESUMED:
                message = "Scan resumed";
                break;
            case SCAN_PAUSED:
                message = "Scan paused";
                break;
        }

        final String finalMessage = message;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MainActivity.this, finalMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }


    private String generateBeaconText() {
        StringBuffer sb = new StringBuffer();
        for (BeaconWithBattery beacon : beacons.values()) {
            sb.append(beacon.getMajor());
            sb.append(":");
            sb.append(beacon.getMinor());
            sb.append(" = ");
            sb.append(beacon.getChargeState());
            sb.append(" sent to server: ");
            sb.append(beacon.sentToServer());
            sb.append("\n");
        }
        return sb.toString();
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