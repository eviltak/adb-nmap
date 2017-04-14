package net.eviltak.adbnmap.net

import org.junit.Test

import org.junit.Assert.*
import org.junit.experimental.runners.Enclosed
import org.junit.runner.RunWith
import java.net.InetSocketAddress
import java.net.Socket
import java.nio.ByteBuffer
import java.nio.ByteOrder

@RunWith(Enclosed::class)
class AdbProtocolTest {
    class SendTestMessage {
        @Test
        fun sendTestMessageTest() {
            Socket().use {
                // TODO: Refactor host address into easily changeable constant
                // Timeout increased specifically for testing
                it.connect(InetSocketAddress("192.168.49.104", 5555), 500)

                val protocol = AdbProtocol()

                protocol.sendTestMessage(it)

                // Basic response test to check whether test message was correct
                // Comprehensive verification is performed by Protocol.hostUsesProtocol
                val inBuffer = ByteArray(128)
                it.getInputStream().read(inBuffer)

                val cmd = ByteBuffer.wrap(inBuffer).order(ByteOrder.LITTLE_ENDIAN).int
                assertEquals("Invalid command returned", 0x48545541, cmd)
            }
        }
    }

    class HostUsesProtocol {
        @Test
        fun hostUsesProtocolTest() {
            Socket().use {
                it.connect(InetSocketAddress("192.168.49.104", 5555), 500)

                val protocol = AdbProtocol()

                assertTrue("hostUsesProtocol returned false", protocol.hostUsesProtocol(it))
            }
        }
    }
}