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

import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import java.nio.ByteBuffer
import java.nio.ByteOrder

@RunWith(Parameterized::class)
class AdbTransportMessageTest(val message: AdbTransportMessage, val expectedByteArray: ByteArray) {
    companion object {
        @JvmStatic
        @Parameterized.Parameters
        fun data(): Collection<Array<Any>> {
            return listOf(
                    arrayOf(AdbTransportMessage(AdbTransportMessage.Command.CNXN, 0x01000000, 256 * 1024, ""),
                            ByteBuffer.allocate(4 * 6).order(ByteOrder.LITTLE_ENDIAN)
                                    .putInt(0x4e584e43)
                                    .putInt(0x01000000)
                                    .putInt(256 * 1024)
                                    .putInt(0)
                                    .putInt(0)
                                    .putInt(0x4e584e43 xor -1)
                                    .array()
                    ),
                    arrayOf(AdbTransportMessage(AdbTransportMessage.Command.WRTE, 15, 20, "some_payload"),
                            ByteBuffer.allocate(4 * 6 + "some_payload".length).order(ByteOrder.LITTLE_ENDIAN)
                                    .putInt(0x45545257)
                                    .putInt(15)
                                    .putInt(20)
                                    .putInt("some_payload".length)
                                    .putInt(0)
                                    .putInt(0x45545257 xor -1)
                                    .put("some_payload".toByteArray(Charsets.US_ASCII))
                                    .array()
                    )
            )
        }
    }

    @Test
    fun toByteArrayTest() {
        val messageByteArray = message.toByteArray()

        Assert.assertTrue("toByteArray returned ${String(messageByteArray)}",
                          messageByteArray contentEquals expectedByteArray)
    }
}
