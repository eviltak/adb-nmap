package net.eviltak.adbnmap.net

import java.net.Socket
import java.nio.ByteBuffer
import java.nio.ByteOrder

/**
 * The protocol used for communication between the ADB server and the target device's adbd daemon.
 */
class AdbProtocol : Protocol {
    /**
     * Send a test message in this communication protocol to a host through the [socket], eliciting a response
     * from the target host confirming whether it supports this protocol or not.
     *
     * @param socket The socket connected to the host to which to send the test message.
     */
    override fun sendTestMessage(socket: Socket) {
        val outBuffer = ByteBuffer.allocate(4 * 6).order(ByteOrder.LITTLE_ENDIAN)

        // TODO: Refactor into an AdbMessage class if needed
        outBuffer.putInt(0x4e584e43)            // command: A_CNXN
        outBuffer.putInt(0x01000000)            // arg0:    version
        outBuffer.putInt(256 * 1024)            // arg1:    maxdata
        outBuffer.putInt(0)                     // data_length
        outBuffer.putInt(0)                     // data_checksum
        outBuffer.putInt(0x4e584e43 xor -1)     // magic (command ^ 0xffffffff)

        socket.getOutputStream().write(outBuffer.array())
    }

    /**
     * Verify that the host the [socket] is connected to uses this communication protocol.
     *
     * @param socket The socket connected to the host.
     *
     * @return true if the connected host uses this protocol, false otherwise.
     */
    override fun verifyHostUsesProtocol(socket: Socket): Boolean {
        return false
    }
}