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
import java.net.SocketAddress

class SocketConnector(val connectionTimeout: Int) {
    fun connect(socketAddress: SocketAddress): Socket {
        val socket = Socket()
        socket.connect(socketAddress, connectionTimeout)
        return socket
    }

    fun <R> connectAndUse(socketAddress: SocketAddress, block: (Socket) -> R): R {
        return connect(socketAddress).use(block)
    }

    fun tryConnect(socketAddress: SocketAddress, block: (Socket) -> Boolean): Boolean {
        val socket: Socket

        try {
            socket = connect(socketAddress)
        }
        catch (_: Exception) {
            return false
        }

        return socket.use(block)
    }
}
