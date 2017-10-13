# Stroeer Configuration SDK Changelog

This file lists all notable changes to the Stroeer Configuration SDK.

## Table of versions
<!-- TOC depthFrom:2 depthTo:6 withLinks:1 updateOnSave:1 -->

* [Unreleased](#unreleased)
* [0.2.18](#0218)
* [0.2.13](#0213)

<!-- /TOC -->
## Unreleased
### Added
* New callback method: "onBeaconScanned(BeaconWithBattery beacon)"
    * This method is called whenever a beacon is scanned.
* New method in the BeaconWithBattery class: "getDmc()"
    * Use this method to get a String containing the appropriate DMC for the particular beacon.
* New method in ConfigurationApi: "setUnknownBeaconDisableDuration(long timeInMillis)
    * Use this to change the time a scanned beacon becomes disabled (meaning, the Api will ignore it for that time) if there is no appropriate entry for this beacon on the server. Default: 86400000 milliseconds (one day). Needs to be at least 60000 milliseconds (one minute).
* New method in ConfigurationApi: "setBeaconSendErrorDisableDuration(long timeInMillis)"
    * Use this to change the time a scanned beacon becomes disabled if there multiple errors regarding that beacon (couldn't find it on the server, connection errors, etc.). Default: 60000 milliseconds (one minute). Needs to be at least 60000 milliseconds (one minute).

### Fixed
* Beacons that were scanned without internet connection will be sent to the server with a timestamp when they were scanned, so the time of the beacon update will have this timestamp instead of the time when the beacon update arrives at the server.
* Correctly reports invalid API keys with an InvalidApiKeyException.

### Removed
* The "onBeaconDataChanged(BeaconWithBattery beacon)" won't be called anymore on every beacon scan. Use the new "onBeaconScanned(BeaconWithBattery beacon)" for that instead.


## 0.2.18
released Apr 25, 2017
### Added
* Implemented the CryptoV2 decryption algorithm and decryption key download.

### Removed
* This version sends the battery states to the new beacon management server "beaconadmin". The old server "beaconcrm" (used by Config SDK version 0.2.13) is no longer supported.


## 0.2.13
released Dec 22, 2016
### Added
* First release of the Configuration SDK.
* Supports normal Stroeer beacons as well as beacons using the CryptoV1 decryption algorithm.
* Scans beacons and sends their battery charge state to beacon management server "beaconcrm".