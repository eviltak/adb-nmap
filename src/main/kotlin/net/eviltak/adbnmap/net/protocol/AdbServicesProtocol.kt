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

