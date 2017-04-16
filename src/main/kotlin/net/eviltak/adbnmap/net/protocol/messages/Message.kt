package net.eviltak.adbnmap.net.protocol.messages

interface Message {
    /**
     * Encodes the message into a byte array.
     *
     * @return The resulting byte array.
     */
    fun toByteArray(): ByteArray
}
