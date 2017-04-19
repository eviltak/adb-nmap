/*
 * adb-nmap: An ADB network device discovery and connection library
 * Copyright (C) 2017-present Arav Singhal and adb-nmap contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * The full license can be found in LICENSE.md.
 */

package net.eviltak.adbnmap.net.protocol.messages

import java.nio.ByteBuffer
import java.nio.ByteOrder

class AdbTransportMessage
private constructor(val command: Int, val arg0: Int, val arg1: Int, val dataLength: Int,
                    val dataChecksum: Int, val magic: Int, val payload: String)
    : Message {

    enum class Command(val intValue: Int) {
        SYNC(0x434e5953),
        CNXN(0x4e584e43),
        AUTH(0x48545541),
        OPEN(0x4e45504f),
        OKAY(0x59414b4f),
        CLSE(0x45534c43),
        WRTE(0x45545257)
    }

    companion object {
        /**
         * The header size in bytes.
         */
        const val HEADER_SIZE = 24

        fun representsTransportMessage(byteArray: ByteArray): Boolean {
            val byteBuffer = ByteBuffer.wrap(byteArray).order(ByteOrder.LITTLE_ENDIAN)

            val command = byteBuffer.int

            // Read last integer of header
            byteBuffer.position(AdbTransportMessage.HEADER_SIZE - 4)
            val magic = byteBuffer.int

            return Command.values().any { it.intValue == command } && (command xor magic == -1)
        }
    }

    constructor(command: Command, arg0: Int, arg1: Int, payload: String)
            : this(command.intValue, arg0, arg1, payload.length, 0, command.intValue xor -1, payload)

    /**
     * Encodes the message into a byte array.
     *
     * @return The resulting byte array.
     */
    override fun toByteArray(): ByteArray {
        val byteBuffer = ByteBuffer.allocate(HEADER_SIZE + dataLength).order(ByteOrder.LITTLE_ENDIAN)

        byteBuffer
                .putInt(command)
                .putInt(arg0)
                .putInt(arg1)
                .putInt(dataLength)
                .putInt(dataChecksum)
                .putInt(magic)
                .put(payload.toByteArray(Charsets.US_ASCII))

        return byteBuffer.array()
    }
}
