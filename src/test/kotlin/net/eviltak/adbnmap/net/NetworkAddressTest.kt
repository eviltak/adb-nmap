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

package net.eviltak.adbnmap.net

import org.junit.Assert.assertEquals
import org.junit.Test
import java.net.InetAddress

class NetworkAddressTest {
    class AllPossibleAddressesFrom {
        @Test
        fun allPossibleAddressesFromTest() {
            val ipAddresses = listOf(
                    InetAddress.getByAddress(byteArrayOf(10, 10, 10, 10)),
                    InetAddress.getByAddress(byteArrayOf(12, 13, 14, 11)),
                    InetAddress.getByAddress(byteArrayOf(-1, -1, 0, -1))    // 255.255.0.255
            )

            val ports = listOf(
                    5037,
                    5555
            )

            val expectedResult = arrayOf(
                    NetworkAddress(InetAddress.getByAddress(byteArrayOf(10, 10, 10, 10)), 5037),
                    NetworkAddress(InetAddress.getByAddress(byteArrayOf(10, 10, 10, 10)), 5555),
                    NetworkAddress(InetAddress.getByAddress(byteArrayOf(12, 13, 14, 11)), 5037),
                    NetworkAddress(InetAddress.getByAddress(byteArrayOf(12, 13, 14, 11)), 5555),
                    NetworkAddress(InetAddress.getByAddress(byteArrayOf(-1, -1, 0, -1)), 5037),
                    NetworkAddress(InetAddress.getByAddress(byteArrayOf(-1, -1, 0, -1)), 5555)
            )

            assertEquals(0, (expectedResult subtract allPossibleAddressesFrom(ipAddresses, ports)).size)
        }
    }
}
