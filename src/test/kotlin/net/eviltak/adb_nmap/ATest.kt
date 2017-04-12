package net.eviltak.adb_nmap

import org.junit.Assert.assertEquals
import org.junit.Test
import java.net.Socket
import java.nio.ByteBuffer
import java.nio.ByteOrder

class ATest {
    @Test fun aTest(): Unit {
        Socket("192.168.49.104", 5555).use {
            val inStream = it.getInputStream()
            val outStream = it.getOutputStream()

            val outBuffer = ByteBuffer.allocate(4 * 6).order(ByteOrder.LITTLE_ENDIAN)
            outBuffer.putInt(0x4e584e43)
            outBuffer.putInt(0x01000000)
            outBuffer.putInt(256 * 1024)
            outBuffer.putInt(0)
            outBuffer.putInt(0)
            outBuffer.putInt(0x4e584e43 xor -1)

            outStream.write(outBuffer.array())

            val inBuffer = ByteArray(128)
            inStream.read(inBuffer)

            val cmd = ByteBuffer.wrap(inBuffer).order(ByteOrder.LITTLE_ENDIAN).int
            assertEquals(0x48545541, cmd)
        }
    }
}
