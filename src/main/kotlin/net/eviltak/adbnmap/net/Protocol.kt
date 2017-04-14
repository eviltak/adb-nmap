package net.eviltak.adbnmap.net

import java.net.Socket

/**
 * A communication protocol that may be used by devices on a network.
 */
interface Protocol {
    /**
     * Send a test message in this communication protocol to a host through the [socket], eliciting a response
     * from the target host confirming whether it supports this protocol or not.
     *
     * @param socket The socket connected to the host to which to send the test message.
     */
    fun sendTestMessage(socket: Socket)

    /**
     * Check if the host the [socket] is connected to uses this communication protocol. An implementation would
     * ideally use [sendTestMessage] and listen for a valid response.
     *
     * @param socket The socket connected to the host.
     *
     * @return true if the connected host uses this protocol, false otherwise.
     */
    fun hostUsesProtocol(socket: Socket): Boolean
}