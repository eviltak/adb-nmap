/*
 * adb-nmap: An ADB network device discovery and connection library
 * Copyright (C) 2017-present Arav Singhal and adb-nmap contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * The full license can be found in LICENSE.md.
 */

package net.eviltak.adbnmap.net

import java.net.Socket

typealias DeviceValidator = (Socket) -> Boolean

class NetworkMapper(val validateDevice: DeviceValidator) {
    fun pingDevice(address: NetworkAddress): Boolean {
        return Socket().use {
            try {
                // TODO: Refactor timeout to config class
                it.connect(address.toSocketAddress(), 500)
            }
            catch(_: Exception) {
                return@use false
            }

            return@use validateDevice(it)
        }
    }

    fun retrieveDevicesFrom(addresses: Iterable<NetworkAddress>): List<NetworkAddress> {
        return addresses.filter { pingDevice(it) }
    }
}

