# Stroeer Configuration SDK Changelog

This file lists all notable changes to the Stroeer Configuration SDK.

## Table of versions
<!-- TOC depthFrom:2 depthTo:6 withLinks:1 updateOnSave:1 -->

* [0.2.18](#0218)
* [0.2.13](#0213)

<!-- /TOC -->


# 0.2.18
released Apr 25, 2017
### Added
The Configuration SDK is able to scan and decrypt beacons using the encryption v2. To recognize these beacons they use a fixed UUID c2340cb0-d412-11e6-bf26-cec0c932ce01. Beacons with encryption v2 change their major and minor values from day to day, so the SDK needs to fetch the required decryption keys from the server. Upon starting the scan, the SDK will download these decryption keys. If needed, the SDK automatically downloads more valid keys.

### Removed
Now, the battery charge state of a beacon is sent to the new beacon management server beaconadmin. The old management server beaconcrm is no more used.


# 0.2.13
released Dec 22, 2016

### Added

- _Scanning:_

  To scan for a beacon the app integrating the Configuration SDK needs location permissions and bluetooth. The SDK provides functions to start (`startScan`) and to stop (`stopScan`) beacon scanning. Upon starting the scan for the first time, the app requires an internet connection once to verify the API-key. The SDK detects only beacons with the following UUIDs:
    - 37ecc5b5-dc9f-4d5c-b1ee-7f09146572a2
    - 88084780-6528-11e6-bdf4-0800200c9a66

  If the beacon scanning is started the SDK scans for beacons no matter if the app integrating the SDK is in foreground or background. Furthermore, beacon scanning will still be performed when the app was closed. Beacon scanning also does not need a permanent internet connection.

  Furthermore, the SDK can scan for beacons which shuffle the major and minor values on a random basis (Crypto V1 procedure). The SDK is able to decrypt the shuffled values and provide the original major and minor values.

-  _Battery charge state of a beacon:_

  The Configuration SDK determines the battery charge state of a beacon and sends this value to the beacon management server beaconcrm.
