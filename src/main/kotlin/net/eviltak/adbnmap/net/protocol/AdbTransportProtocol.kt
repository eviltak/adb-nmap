package net.eviltak.adbnmap.net.protocol

import java.net.Socket
import java.net.SocketTimeoutException
import java.nio.ByteBuffer
import java.nio.ByteOrder

/**
 * The protocol used for communication between the ADB server and the target device's adbd daemon.
 *
 * https://github.com/android/platform_system_core/blob/master/adb/protocol.txt
 */
class AdbTransportProtocol(override val socket: Socket) : Protocol {
    companion object {
        /**
         * The header size in bytes.
         */
        const val HEADER_SIZE = 24

        private val COMMAND_CONSTANTS = mapOf(
                "A_SYNC" to 0x434e5953,
                "A_CNXN" to 0x4e584e43,
                "A_AUTH" to 0x48545541,
                "A_OPEN" to 0x4e45504f,
                "A_OKAY" to 0x59414b4f,
                "A_CLSE" to 0x45534c43,
                "A_WRTE" to 0x45545257
        )
    }

    /**
     * Send a test message in this communication protocol to a host through the [socket], eliciting a response
     * from the target host confirming whether it supports this protocol or not.
     */
    override fun sendTestMessage() {
        val outBuffer = ByteBuffer.allocate(4 * 6).order(ByteOrder.LITTLE_ENDIAN)

        // TODO: Refactor into an AdbMessage class if needed
        outBuffer.putInt(COMMAND_CONSTANTS["A_CNXN"]!!)             // command: A_CNXN
        outBuffer.putInt(0x01000000)                                // arg0:    version
        outBuffer.putInt(256 * 1024)                                // arg1:    maxdata
        outBuffer.putInt(0)                                         // data_length
        outBuffer.putInt(0)                                         // data_checksum
        outBuffer.putInt(COMMAND_CONSTANTS["A_CNXN"]!! xor -1)      // magic (command ^ 0xffffffff)

        socket.getOutputStream().write(outBuffer.array())
    }

    /**
     * Check if the host the [socket] is connected to uses this communication protocol.
     *
     * @return true if the connected host uses this protocol, false otherwise.
     */
    override fun hostUsesProtocol(): Boolean {
        sendTestMessage()

        val inArray = ByteArray(HEADER_SIZE)

        try {
            // TODO: Convert to modifiable option
            socket.soTimeout = 500
            socket.getInputStream().read(inArray)
        }
        catch (_: SocketTimeoutException) {
            return false
        }

        val inBuffer = ByteBuffer.wrap(inArray).order(ByteOrder.LITTLE_ENDIAN)

        val command = inBuffer.int

        // Read last integer
        inBuffer.position(HEADER_SIZE - 4)
        val magic = inBuffer.int

        return COMMAND_CONSTANTS.containsValue(command) && (command xor magic == -1)
    }
}
