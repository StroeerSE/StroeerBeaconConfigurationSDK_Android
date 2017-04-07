# Ströer Configuration SDK

## Purpose of the SDK
With the ConfigurationSDK you are scanning for special beacons of the Favendo GmbH. This scanning can also be in the background, while your android application is not visible.

The SDK allows full offline functionality. You need to set up the SDK once correctly within your application. Once done, it can be set up without an internet connection next time.

The SDK receives the current charge-state of each beacon and sends this information to the backend (server). You are now able to see this information in the corresponding dashboard.

## Integrate the SDK into a project
In the GitHub you will find a demo application. That application shows how to set up the configuration-sdk-project.

Here is a quick guide that tells you what to do:

First add the following lines to the "build.gradle"-file of your root project
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
Then add the following lines to the "build.gradle"-file of the app-module. Be aware that you might have to update the version numbers according to the aars you found in the demo-application.
```bash
dependencies {
    compile('de.stroeer:stroeerConfigurationSdk:x.y.z@aar')
}
```

## Usage
### Prerequisites
Of course the bluetooth adapter of the device has to be enabled, in order to scan for beacons. But since Android 6.0 it is necessary to also enable Location-Services and to grant the ACCESS_COARSE_LOCATION-Permission explicitly in the app-permiossion settings. This means the sdk-user has to implement this in a way so the app-user doesn't have to open the permission settings of the Android OS. There is an implementation example of this in the demo-project of the configurationSdk.

To use the whole functionality of the SDK, it is necessary to declare some permissions. This is already done by the sdk's own Android-Manifest:

#### Predefined permissions by sdk
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
The SDK features automatic data-upload after the internet connection got lost and than reconnects. To recognize those changes the following permissions are needed:
```xml
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>
```
Since Android 6.0 it is necessary to have Location Permission in order to scan for beacons:
```xml
<uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION"/>
```
### Main Classes
As you can see in the demo-project which is delivered, the most important class is `ConfigurationApi`.

`ConfigurationApi` gives access to all functionality of the ConfigurationSDK.
After you have got an instance of `ConfigurationApi` you have to set the ApiKey, which confirms you to be a valid user of the sdk. The next step is to register a listener to get some information about the current state of the configurationSdk. Don't forget to unregister this listener after you have closed the activity.

```java
public class MyActivity extends Activity implements ConfigurationApi.Listener {

    public static final String API_KEY = "type apikey here";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ConfigurationApi.getInstance(this).setApiKey(API_KEY);
    }

    @Override
    protected void onResume() {
        super.onResume();
        StroeerProxityApi.getInstance(this).registerConfigurationListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        StroeerProxityApi.getInstance(this).unregisterConfigurationListener(this);
    }

    @Override
    public void onMessage(StroeerProxityApi stroeerProxityApi, Message message, boolean isNew) {

    }

    @Override
    public void onStatusGained(StroeerProxityApi stroeerProxityApi, SdkStatus status, boolean isNew) {

    }

    @Override
    public void onStatusRevoked(StroeerProxityApi stroeerProxityApi, SdkStatus status, boolean isNew) {

    }
}
```

### Scan process
#### Start Scanning

The background-service will start scanning right after you have created an instance `(StroeerProxityApi.getInstance(this))`. The background server maintains itself and will return to the desired state after it get closed by the system.

#### Stop Scanning

The scanning will stop if some of the following points happen:

 - Bluetooth is turned off
 - LocationServices are turend off
 - LocationPermission was revoked
 
## Further Information
For further information the whole api is documented as JavaDoc. You can find this JavaDoc inside of the zip-file in folder documentation.
