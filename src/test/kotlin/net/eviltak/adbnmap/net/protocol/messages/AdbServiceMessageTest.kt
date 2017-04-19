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

import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized::class)
class AdbServiceMessageTest(val expectedLengthSegment: String, val messageString: String) {
    companion object {
        @JvmStatic
        @Parameterized.Parameters
        fun data(): Collection<Array<Any>> {
            return listOf(
                    arrayOf<Any>("000C", "host:version"),
                    arrayOf<Any>("0018", "some_device:some_service")
            )
        }
    }

    @Test
    fun toByteArrayTest() {
        val message = AdbServiceMessage(messageString)
        val messageByteArray = message.toByteArray()

        val expectedByteArray = (expectedLengthSegment + messageString).toByteArray(Charsets.US_ASCII)

        assertTrue("toByteArray returned ${String(messageByteArray, Charsets.US_ASCII)}",
                   messageByteArray contentEquals expectedByteArray)
    }
}
