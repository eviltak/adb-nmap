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
