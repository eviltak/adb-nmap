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

import com.nhaarman.mockito_kotlin.*
import net.eviltak.adbnmap.net.protocol.AdbServicesProtocol
import org.junit.Test
import java.net.InetSocketAddress

class NetworkMapperTest {
    @Test
    fun getDevicesInNetworkTest() {
        val networkSize = 1000

        val networkMapper = NetworkMapper(mock<SocketConnector>(), { AdbServicesProtocol(it) })
        val networkMapperSpy = spy(networkMapper)

        networkMapperSpy.getDevicesInNetwork(List(networkSize, { InetSocketAddress(0) }))

        verify(networkMapperSpy, times(networkSize)).ping(any())
    }
}
