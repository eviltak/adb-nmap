# adb-nmap
An ADB network device discovery and connection library

[![Build Status](https://travis-ci.org/eviltak/adb-nmap.svg?branch=master)](https://travis-ci.org/eviltak/adb-nmap)

## Why adb-nmap
adb-nmap looks for ADB-supported devices on a network and optionally connects the ADB server (if there is one on the network) to them. This is much easier than having to look up the device's local network address and using the `adb connect` command, especially when done through an IDE or from the remote device itself.
