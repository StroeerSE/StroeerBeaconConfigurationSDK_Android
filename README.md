# Ströer Configuration SDK

## Table of contents
1. [Purpose of the SDK](#purpose-of-the-sdk)
2. [Integrate the SDK into a project](#integrate-the-sdk-into-a-project)
3. [Usage](#usage)
    1. [Prerequisites](#prerequisites)
    2. [Predefined permissions by SDK](#predefined-permissions-by-sdk)
    3. [Main Classes](#main-classes)
    4. [Scan process](#scan-process)
        1. [Start Scanning](#start-scanning)
        2. [Stop Scanning](#stop-scanning)
        3. [Energy save mode](#energy-save-mode)
        4. [Scan status transitions](#scan-status-transitions)
4. [Further Information](#further-information)

## Purpose of the SDK
With the Configuration SDK you are scanning for special beacons of the Favendo GmbH. This scanning can also be in the background, while your android application is not visible.

The SDK allows full offline functionality. You need to set up the SDK once correctly within your application. Once done, it can be set up without an internet connection next time.

The SDK receives the current charge state of each beacon and sends this information to the backend (server). You are then able to see this information in the corresponding dashboard.

## Integrate the SDK into a project
In this GitHub repository you will find a demo application. That application shows how to set up the Configuration SDK.

Here is a quick guide that tells you what to do:

First add the following lines to the "build.gradle" file of your root project
```bash
allprojects {
    repositories {
        jcenter()
        maven {
             url 'http://maven.match2blue.com/nexus/content/repositories/StroeerGroup/'
            credentials {
                username 'StroeerUser'
                password 'StroeerPW2016'
            }
        }
    }
}
```
Then add the following lines to the "build.gradle" file of the app module. Be aware that you might have to update the version numbers according to the aars you found in the demo application.
```bash
dependencies {
    compile('de.stroeer:stroeerConfigurationSdk:x.y.z@aar') {
        exclude group: "com.android.support", module: "support-v4"
        transitive = true
    }
}
```

The current version is 0.3.0.

## Usage
### Prerequisites
Of course the bluetooth adapter of the device has to be enabled in order to scan for beacons. However, since Android 6.0 it is also necessary to enable location services and to grant the ACCESS_COARSE_LOCATION permission explicitly in the app permission settings. This means a developer using the SDK should implement this in a way so the app user doesn't have to open the permission settings of the Android OS. There is an implementation example of this in the demo application of the Configuration SDK.

To use the whole functionality of the SDK, it is necessary to declare such permissions. This is already done by the SDK's own Android-Manifest:

### Predefined permissions by SDK
To start and stop scanning for bluetooth low energy devices:
```xml
<uses-permission android:name="android.permission.BLUETOOTH" />
```
To check if bluetooth is available and running:
```xml
<uses-permission android:name="android.permission.BLUETOOTH_ADMIN" />
```
To upload analytics data to the backend:
```xml
<uses-permission android:name="android.permission.INTERNET" />
```
The SDK features automatic data upload after the internet connection got lost and than reconnects. To recognize those changes the following permissions are needed:
```xml
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>
```
Since Android 6.0 it is necessary to have Location Permission in order to scan for beacons:
```xml
<uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION"/>
```
### Main Classes
As you can see in the demo application which is delivered, the most important class is `ConfigurationApi`.

`ConfigurationApi` gives access to all functionality of the Configuration SDK.
After you have got an instance using `ConfigurationApi.getInstance(Context context)` you have to set the ApiKey using the `setApiKey(String apiKey)` method, which confirms you to be a valid user of the SDK.

The next step is to register a `ConfigurationApi.Listener` using the `registerConfigurationListener(ConfigurationApi.Listener listener)` method to get some information about the current state of the Configuration SDK. Don't forget to unregister this listener using `unregisterConfigurationListener(ConfigurationApi.Listener listener)` after you have closed the activity.

Now, if you call `startScan()`, your ApiKey is being verified if the device currently has or once it has gained an internet connection. Once this is successful, the scan is started.

A very basic example on how to implement the Configuration SDK is shown below:
```java
import android.app.Activity;
import android.os.Bundle;

import de.stroeer.configurationsdk.ConfigurationApi;
import de.stroeer.configurationsdk.model.BeaconWithBattery;

public class MyActivity extends Activity implements ConfigurationApi.Listener {

    public static final String API_KEY = "type apikey here";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ConfigurationApi.getInstance(this).setApiKey(API_KEY);
        ConfigurationApi.getInstance(this).startScan();
    }

    @Override
    protected void onResume() {
        super.onResume();
        ConfigurationApi.getInstance(this).registerConfigurationListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        ConfigurationApi.getInstance(this).unregisterConfigurationListener(this);
    }

    @Override
    public void onBeaconDataChanged(BeaconWithBattery beacon) {
    }

    @Override
    public void onBeaconDataCleared() {
    }

    @Override
    public void onFailure(String message) {
    }

    @Override
    public void onBeaconScanned(BeaconWithBattery beacon) {
    }

    @Override
    public void onScanStatusChanged(ConfigurationApi.ScanStatusCode scanStatusCode) {
    }
}
```
A more detailed setup can be found in the demo application.

### Scan process

Since version 0.2.18 the CryptoV2 algorithm is also supported. To decrypt these cryptoV2 beacons it is necessary to have an internet connection so the decryption keys can be loaded. The decryption keys are loaded up to 14 days in the future. In this period (14days) the SDK can decrypt cryptoV2 beacon even if offline.

#### Start Scanning

The background service will start scanning right after you have called `startScan()` and your api key was verified, if this did not happen already. The background service maintains itself and will return to its former state after your app was closed.

#### Stop Scanning

The scanning will stop if one of the following events occur:

 - `stopScan()` was called
 - Bluetooth is turned off
 - LocationServices are turned off
 - LocationPermission was revoked

#### Energy save mode

Once the scan is started, the Configuration SDK will scan for beacons without interruption until the scan is stopped manually. However, as this may significantly drain a device's battery, it automatically pauses the scan once the device's motion sensors, if available, do not detect significant movement for a predefined duration of 10 minutes.

This duration can be changed using the `BaseApi.setStandStillDetectionDuration(long timeInMinutes)` method. Note: you need to use a value which is greater or equal to 1 and less or equal to 20.

If you wish to reset this duration value, simply call the `BaseApi.resetStandStillDetectionDuration()` method. The current default is 10 minutes.

If needed, you can get its current value using the `BaseApi.getStandStillDetectionDurationAsync(BaseApi.AsynCallback<Long> method)`.

#### Scan status transitions

The Listener of the `ConfigurationApi` is able to tell you about scan status transitions using the `onScanStatusChanged(ScanStatusCode newStatus)` callback. It is called if one of these events occur:

- `ConfigurationApi.startScan()` was called. The `newStatus` will be `ScanStatusCode.SCAN_STARTED`.
- `ConfigurationApi.stopScan()` was called. The `newStatus` will be `ScanStatusCode.SCAN_STOPPED`.
- the scan was paused due to the device being standing still for a sufficient duration. The `newStatus` will be `ScanStatusCode.SCAN_PAUSED`.
- the scan was resumed due to the device being significantly moved while the scan was paused. The `newStatus` will be `ScanStatusCode.SCAN_RESUMED`.

## Further Information
For further information the whole API is documented as JavaDoc. You can find this JavaDoc inside of the zip file in the javaDoc folder.
