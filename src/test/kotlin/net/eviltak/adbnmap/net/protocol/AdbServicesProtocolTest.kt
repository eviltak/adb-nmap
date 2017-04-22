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

package net.eviltak.adbnmap.net.protocol

import com.nhaarman.mockito_kotlin.*
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.experimental.runners.Enclosed
import org.junit.runner.RunWith
import java.io.*
import java.net.Socket
import java.net.SocketTimeoutException

@RunWith(Enclosed::class)
class AdbServicesProtocolTest {
    class SendTestMessage {
        @Test
        fun sendTestMessageTest() {
            val byteArrayOutputStream = ByteArrayOutputStream()

            val mockedSocket = mock<Socket> {
                on { getOutputStream() } doReturn byteArrayOutputStream
            }

            val servicesProtocol = AdbServicesProtocol(mockedSocket)
            servicesProtocol.sendTestMessage()

            assertTrue("Wrong command \"${String(byteArrayOutputStream.toByteArray(),
                                                 Charsets.US_ASCII)}\" written to socket",

                       byteArrayOutputStream.toByteArray().contentEquals(
                               "000Chost:version".toByteArray(Charsets.US_ASCII))
            )
        }
    }

    class HostUsesProtocol {
        @Test
        fun hostDoesUseProtocolTest() {
            val byteArrayInputStream = ByteArrayInputStream("OKAY0000".toByteArray(Charsets.US_ASCII))
            val byteArrayOutputStream = ByteArrayOutputStream()

            val mockedSocket = mock<Socket> {
                on { getInputStream() } doReturn byteArrayInputStream
                on { getOutputStream() } doReturn byteArrayOutputStream
            }

            val servicesProtocol = AdbServicesProtocol(mockedSocket)

            assertTrue("hostUsesProtocol returned false", servicesProtocol.hostUsesProtocol())
        }

        @Test
        fun invalidDataReceivedTest() {
            val byteArrayInputStream = ByteArrayInputStream("FAIL0000".toByteArray(Charsets.US_ASCII))
            val byteArrayOutputStream = ByteArrayOutputStream()

            val mockedSocket = mock<Socket> {
                on { getInputStream() } doReturn byteArrayInputStream
                on { getOutputStream() } doReturn byteArrayOutputStream
            }

            val servicesProtocol = AdbServicesProtocol(mockedSocket)

            assertFalse("hostUsesProtocol returned true", servicesProtocol.hostUsesProtocol())
        }

        @Test
        fun dataReceiveTimeoutTest() {
            val blockingInputStream = mock<InputStream> {
                on { read(any()) } doAnswer doAnswer@ {
                    throw SocketTimeoutException()
                }
            }
            val byteArrayOutputStream = ByteArrayOutputStream()

            val mockedSocket = mock<Socket> {
                on { getInputStream() } doReturn blockingInputStream
                on { getOutputStream() } doReturn byteArrayOutputStream
            }

            val servicesProtocol = AdbServicesProtocol(mockedSocket)

            assertFalse("hostUsesProtocol returned true", servicesProtocol.hostUsesProtocol())
        }
    }
}
