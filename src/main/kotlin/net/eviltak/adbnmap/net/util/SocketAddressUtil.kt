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

package net.eviltak.adbnmap.net.util

import java.net.*

fun isValidIpAddress(ipAddress: String): Boolean {
    try {
        val uri = URI("scheme://$ipAddress")

        if (uri.host == null) {
            throw URISyntaxException(uri.toString(), "Invalid IP Address")
        }

        return true
    }
    catch (_: URISyntaxException) {
        return false
    }
}

fun subnetAddresses(subnet: String, port: Int): Iterable<SocketAddress> {
    return (0..255)
            .asSequence()
            .map { "$subnet.$it" }
            .map {
                if (!isValidIpAddress(it)) {
                    throw IllegalArgumentException("Invalid subnet \"$subnet\" passed")
                }
                InetSocketAddress(it, port)
            }
            .toList()
            .asIterable()
}
