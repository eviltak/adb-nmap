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

import java.net.*

data class NetworkAddress(val ipAddress: InetAddress, val port: Int) {
    fun toSocketAddress(): SocketAddress {
        return InetSocketAddress(ipAddress, port)
    }
}

fun allPossibleAddressesFrom(ipAddresses: Iterable<InetAddress>,
                             ports: Iterable<Int>): Iterable<NetworkAddress> {
    return ipAddresses.flatMap { ipAddress ->
        ports.map { port -> NetworkAddress(ipAddress, port) }
    }
}
