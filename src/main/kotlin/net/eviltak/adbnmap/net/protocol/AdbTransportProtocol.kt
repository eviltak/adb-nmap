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

import net.eviltak.adbnmap.net.protocol.messages.AdbTransportMessage
import java.net.Socket
import java.net.SocketTimeoutException

/**
 * The protocol used for communication between the ADB server and the target device's adbd daemon.
 *
 * https://github.com/android/platform_system_core/blob/master/adb/protocol.txt
 */
class AdbTransportProtocol(override val socket: Socket) : Protocol<AdbTransportMessage> {

    /**
     * Send a test message in this communication protocol to a host through the [socket], eliciting a response
     * from the target host confirming whether it supports this protocol or not.
     */
    override fun sendTestMessage() {
        sendMessage(AdbTransportMessage(AdbTransportMessage.Command.CNXN, 0x01000000, 256 * 1024, ""))
    }

    /**
     * Check if the host the [socket] is connected to uses this communication protocol.
     *
     * @return true if the connected host uses this protocol, false otherwise.
     */
    override fun hostUsesProtocol(): Boolean {
        sendTestMessage()

        val inByteArray = ByteArray(AdbTransportMessage.HEADER_SIZE)

        try {
            // TODO: Convert to modifiable option
            socket.soTimeout = 500
            socket.getInputStream().read(inByteArray)
        }
        catch (_: SocketTimeoutException) {
            return false
        }

        return AdbTransportMessage.representsTransportMessage(inByteArray)
    }
}
