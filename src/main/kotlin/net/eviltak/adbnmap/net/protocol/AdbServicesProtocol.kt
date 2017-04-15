package net.eviltak.adbnmap.net.protocol

import java.net.Socket

/**
 * The protocol used by the ADB client to communicate with the ADB server
 */
class AdbServicesProtocol(override val socket: Socket) : Protocol {

    /**
     * Send a test message in this communication protocol to a host through the [socket], eliciting a response
     * from the target host confirming whether it supports this protocol or not.
     *
     * @param socket The socket connected to the host to which to send the test message.
     */
    override fun sendTestMessage() {

    }

    /**
     * Check if the host the [socket] is connected to uses this communication protocol.
     *
     * @param socket The socket connected to the host.
     *
     * @return true if the connected host uses this protocol, false otherwise.
     */
    override fun hostUsesProtocol(): Boolean {
        return false
    }
}
