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

import net.eviltak.adbnmap.net.protocol.Protocol
import java.net.Socket
import java.net.SocketAddress

/**
 * Scans networks for devices which use the specified protocol.
 *
 * @param P The type of protocol devices are expected to use.
 *
 * @property protocolFromSocket Creates an instance of the protocol of type [P] from the socket to be used
 * to connect to the host.
 */
class NetworkMapper<out P: Protocol<*>>(val protocolFromSocket: (Socket) -> P) {
    /**
     * Pings the host at [socketAddress] if such a host exists to check whether it supports the
     * specified protocol.
     *
     * @param socketAddress The socket address of the host to ping.
     * @return True if the host at [socketAddress] exists and supports the specified protocol.
     */
    fun ping(socketAddress: SocketAddress): Boolean {
        return Socket().use {
            try {
                // TODO: Refactor timeout to config class
                it.connect(socketAddress, 500)
                val protocol = protocolFromSocket(it)
                protocol.hostUsesProtocol()
            }
            catch(_: Exception) {
                false
            }
        }
    }

    /**
     * Pings all hosts in [socketAddresses] and returns the address of all devices that support the
     * protocol [P].
     *
     * @param socketAddresses The addresses to check.
     * @return A [List] containing the addresses of all devices in [socketAddresses] that support the
     * protocol [P].
     */
    fun filterDevices(socketAddresses: Iterable<SocketAddress>): List<SocketAddress> {
        return socketAddresses.filter { ping(it) }
    }
}

