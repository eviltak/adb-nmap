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

/**
 * The protocol used by the ADB client to communicate with the ADB server.
 */
class AdbServicesProtocol(override val socket: Socket) : Protocol<AdbServiceMessage> {
    /**
     * Send a test message in this communication protocol to a host through the [socket], eliciting a response
     * from the target host confirming whether it supports this protocol or not.
     */
    override fun sendTestMessage() {
        TODO()
    }

    /**
     * Check if the host the [socket] is connected to uses this communication protocol.
     *
     * @return true if the connected host uses this protocol, false otherwise.
     */
    override fun hostUsesProtocol(): Boolean {
        return false
    }
}

