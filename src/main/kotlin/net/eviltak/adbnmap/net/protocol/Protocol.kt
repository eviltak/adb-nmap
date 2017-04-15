package net.eviltak.adbnmap.net.protocol

import java.net.Socket

/**
 * A communication protocol that may be used by devices on a network.
 */
interface Protocol {
    /**
     * The socket connected to the target host.
     */
    val socket: Socket

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
