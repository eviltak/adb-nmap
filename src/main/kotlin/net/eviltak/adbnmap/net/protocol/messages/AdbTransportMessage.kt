package net.eviltak.adbnmap.net.protocol.messages

class AdbTransportMessage
private constructor(val command: Int, val arg0: Int, val arg1: Int, val dataLength: Int,
                    val dataChecksum: Int, val magic: Int, val payload: String)
    : Message {

    constructor(command: Int, arg0: Int, arg1: Int, payload: String)
            : this(command, arg0, arg1, payload.length, 0, command xor -1, payload)

    /**
     * Encodes the message into a byte array.
     *
     * @return The resulting byte array.
     */
    override fun toByteArray(): ByteArray {
        TODO()
    }
}
