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

package net.eviltak.adbnmap.net.util

import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.experimental.runners.Enclosed
import org.junit.runner.RunWith

@RunWith(Enclosed::class)
class SocketAddressUtilTest {
    class SubnetAddresses {
        @Test
        fun subnetAddressesTest() {
            val subnet = "11.11.11"
            val port = 5555
            var addressIndex = 0

            subnetAddresses(subnet, port).forEach { address ->
                val expected = "$subnet.${addressIndex++}:$port"
                assertEquals(expected, address.toString().substring(1))
            }

            assertEquals(256, addressIndex)
        }

        @Test
        fun invalidSubnetTest() {
            // TODO: Complete test
            print(subnetAddresses("1234", 555).iterator().next().toString())
        }
    }
}
