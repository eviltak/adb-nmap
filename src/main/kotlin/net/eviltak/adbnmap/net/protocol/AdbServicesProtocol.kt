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

package net.eviltak.adbnmap.net.protocol

import net.eviltak.adbnmap.net.protocol.messages.AdbServiceMessage
import java.net.Socket
import java.net.SocketTimeoutException

/**
 * The protocol used by the ADB client to communicate with the ADB server.
 */
class AdbServicesProtocol(override val socket: Socket) : Protocol<AdbServiceMessage> {
    companion object {
        const val ADB_SERVER_PORT = 5037
    }

    /**
     * Send a test message in this communication protocol to a host through the [socket], eliciting a response
     * from the target host confirming whether it supports this protocol or not.
     */
    override fun sendTestMessage() {
        sendMessage(AdbServiceMessage("host:version"))
    }

    /**
     * Check if the host the [socket] is connected to uses this communication protocol.
     *
     * @return true if the connected host uses this protocol, false otherwise.
     */
    override fun hostUsesProtocol(): Boolean {
        sendTestMessage()

        val inByteArray = ByteArray(128)

        // We only read once because the response is guaranteed to not be larger than 128 bytes
        try {
            // TODO: Convert to modifiable option
            socket.soTimeout = 500
            socket.getInputStream().read(inByteArray)
        }
        catch (_: SocketTimeoutException) {
            return false
        }

        // Check if OKAY was returned
        return String(inByteArray.sliceArray(0..3), Charsets.US_ASCII) == "OKAY"
    }
}

