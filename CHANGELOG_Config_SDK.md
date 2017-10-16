# Stroeer Configuration SDK Changelog

This file lists all notable changes to the Stroeer Configuration SDK.

## Table of versions
<!-- TOC depthFrom:2 depthTo:6 withLinks:1 updateOnSave:1 -->

* [0.2.18](#0218)
* [0.2.13](#0213)

<!-- /TOC -->

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