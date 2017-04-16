package net.eviltak.adbnmap.net.protocol.messages

class AdbServiceMessage(val message: String) : Message {
    /**
     * Encodes the message into a byte array.
     *
     * @return The resulting byte array.
     */
    override fun toByteArray(): ByteArray {
        val result = "${"%04X".format(message.length)}$message"

        return result.toByteArray(Charsets.US_ASCII)
    }
}
