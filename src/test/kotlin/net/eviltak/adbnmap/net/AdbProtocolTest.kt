package net.eviltak.adbnmap.net

import com.nhaarman.mockito_kotlin.*
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.experimental.runners.Enclosed
import org.junit.runner.RunWith
import java.io.*
import java.net.Socket
import java.net.SocketTimeoutException
import java.nio.ByteBuffer
import java.nio.ByteOrder

@RunWith(Enclosed::class)
class AdbProtocolTest {
    class SendTestMessage {
        @Test
        fun sendTestMessageTest() {
            val byteArrayOutputStream = ByteArrayOutputStream()

            val mockedSocket = mock<Socket> {
                on { getOutputStream() } doReturn byteArrayOutputStream
            }

            val adbProtocol = AdbProtocol()
            adbProtocol.sendTestMessage(mockedSocket)

            val expectedBuffer = ByteBuffer.allocate(4 * 6).order(ByteOrder.LITTLE_ENDIAN)

            expectedBuffer.putInt(0x4e584e43)
            expectedBuffer.putInt(0x01000000)
            expectedBuffer.putInt(256 * 1024)
            expectedBuffer.putInt(0)
            expectedBuffer.putInt(0)
            expectedBuffer.putInt(0x4e584e43 xor -1)

            assertTrue("Data written to socket output stream was invalid",
                       byteArrayOutputStream.toByteArray() contentEquals expectedBuffer.array())
        }
    }

    class HostUsesProtocol {
        @Test
        fun hostDoesUseProtocolTest() {
            val inputBuffer = ByteBuffer.allocate(4 * 6).order(ByteOrder.LITTLE_ENDIAN)

            // Only the command and magic bytes are considered
            inputBuffer.putInt(0x48545541)
            inputBuffer.putInt(0)
            inputBuffer.putInt(0)
            inputBuffer.putInt(0)
            inputBuffer.putInt(0)
            inputBuffer.putInt(0x48545541 xor -1)

            val byteArrayInputStream = ByteArrayInputStream(inputBuffer.array())
            val byteArrayOutputStream = ByteArrayOutputStream()

            val mockedSocket = mock<Socket> {
                on { getInputStream() } doReturn byteArrayInputStream
                on { getOutputStream() } doReturn byteArrayOutputStream
            }

            val adbProtocol = AdbProtocol()

            assertTrue("hostUsesProtocol returned false", adbProtocol.hostUsesProtocol(mockedSocket))
        }

        @Test
        fun invalidDataReceivedTest() {
            val inputBuffer = ByteBuffer.allocate(4 * 2).order(ByteOrder.LITTLE_ENDIAN)

            inputBuffer.putInt(0x18561531)
            inputBuffer.putInt(0x32)

            val byteArrayInputStream = ByteArrayInputStream(inputBuffer.array())
            val byteArrayOutputStream = ByteArrayOutputStream()

            val mockedSocket = mock<Socket> {
                on { getInputStream() } doReturn byteArrayInputStream
                on { getOutputStream() } doReturn byteArrayOutputStream
            }

            val adbProtocol = AdbProtocol()

            assertFalse("hostUsesProtocol returned true", adbProtocol.hostUsesProtocol(mockedSocket))
        }

        @Test
        fun incompleteDataReceivedTest() {
            val inputBuffer = ByteBuffer.allocate(4 * 5).order(ByteOrder.LITTLE_ENDIAN)

            inputBuffer.putInt(0x48545541)
            inputBuffer.putInt(0)
            inputBuffer.putInt(0)
            inputBuffer.putInt(0)
            inputBuffer.putInt(0)

            val byteArrayInputStream = ByteArrayInputStream(inputBuffer.array())
            val byteArrayOutputStream = ByteArrayOutputStream()

            val mockedSocket = mock<Socket> {
                on { getInputStream() } doReturn byteArrayInputStream
                on { getOutputStream() } doReturn byteArrayOutputStream
            }

            val adbProtocol = AdbProtocol()

            assertFalse("hostUsesProtocol returned true", adbProtocol.hostUsesProtocol(mockedSocket))
        }

        @Test
        fun dataReceiveTimeoutTest() {
            val inputBuffer = ByteBuffer.allocate(0).order(ByteOrder.LITTLE_ENDIAN)

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

            val adbProtocol = AdbProtocol()

            assertFalse("hostUsesProtocol returned true", adbProtocol.hostUsesProtocol(mockedSocket))
        }
    }
}