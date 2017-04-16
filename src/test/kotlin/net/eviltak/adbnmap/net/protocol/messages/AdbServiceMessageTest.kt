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
