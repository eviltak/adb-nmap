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

import net.eviltak.adbnmap.net.protocol.messages.Message
import java.net.Socket

/**
 * A communication protocol that may be used by devices on a network.
 *
 * @param M The message type this protocol uses.
 */
interface Protocol<in M : Message> {
    /**
     * The socket connected to the target host.
     */
    val socket: Socket

    /**
     * Send the [message] to the connected host through the [socket].
     *
     * @param message The message to send.
     */
    fun sendMessage(message: M) {
        socket.getOutputStream().write(message.toByteArray())
    }

    /**
     * Send a test message in this communication protocol to a host through the [socket], eliciting a response
     * from the target host confirming whether it supports this protocol or not.
     */
    fun sendTestMessage()

    /**
     * Check if the host the [socket] is connected to uses this communication protocol. An implementation would
     * ideally use [sendTestMessage] and listen for a valid response.
     *
     * @return true if the connected host uses this protocol, false otherwise.
     */
    fun hostUsesProtocol(): Boolean
}

