// port-lint: source lib.rs
package io.github.kotlinmania.urlencoding

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

class LibTest {

    @Test
    fun itEncodesSuccessfully() {
        val expected = "this%20that"
        assertEquals(expected, encode("this that"))
    }

    @Test
    fun itEncodesSuccessfullyEmoji() {
        val emojiString = "👾 Exterminate!"
        val expected = "%F0%9F%91%BE%20Exterminate%21"
        assertEquals(expected, encode(emojiString))
    }

    @Test
    fun itDecodesSuccessfully() {
        val expected = "this that"
        val encoded = "this%20that"
        assertEquals(expected, decode(encoded).getOrThrow())
    }

    @Test
    fun itDecodesSuccessfullyEmoji() {
        val expected = "👾 Exterminate!"
        val encoded = "%F0%9F%91%BE%20Exterminate%21"
        assertEquals(expected, decode(encoded).getOrThrow())
    }

    @Test
    fun itDecodesUnsuccessfullyEmoji() {
        val badEncodedString = "👾 Exterminate!"
        assertEquals(badEncodedString, decode(badEncodedString).getOrThrow())
    }

    @Test
    fun misc() {
        assertEquals(3, fromHexDigit('3'.code.toByte()))
        assertEquals(10, fromHexDigit('a'.code.toByte()))
        assertEquals(15, fromHexDigit('F'.code.toByte()))
        assertNull(fromHexDigit('G'.code.toByte()))
        assertNull(fromHexDigit(9.toByte()))

        assertEquals("pureascii", encode("pureascii"))
        assertEquals("pureascii", decode("pureascii").getOrThrow())
        assertEquals("", encode(""))
        assertEquals("", decode("").getOrThrow())
        assertEquals("%26a%25b%21c.d%3Fe", encode("&a%b!c.d?e"))
        assertEquals("%00", encode("\u0000"))
        assertEquals("%00x", encode("\u0000x"))
        assertEquals("x%00", encode("x\u0000"))
        assertEquals("x%00x", encode("x\u0000x"))
        assertEquals("aa%00%00bb", encode("aa\u0000\u0000bb"))
        assertEquals("\u0000", decode("\u0000").getOrThrow())
        assertTrue(decode("%F0%0F%91%BE%20Hello%21").isFailure)
        assertEquals("this that", decode("this%20that").getOrThrow())
        assertEquals("this that%", decode("this%20that%").getOrThrow())
        assertEquals("this that%2", decode("this%20that%2").getOrThrow())
        assertEquals("this that%%", decode("this%20that%%").getOrThrow())
        assertEquals("this that%2%", decode("this%20that%2%").getOrThrow())
        assertEquals("this%2that", decode("this%2that").getOrThrow())
        assertEquals("this%%2that", decode("this%%2that").getOrThrow())
        assertEquals("this%2x&that", decode("this%2x%26that").getOrThrow())
        // assertEquals("this%2&that", decode("this%2%26that").getOrThrow())
    }

    @Test
    fun lazyWriter() {
        val s = StringBuilder("he")
        Encoded("llo").appendTo(s)
        assertEquals("hello", s.toString())

        assertEquals("hello", Encoded("hello").toString())
        assertEquals("hello", "${Encoded("hello")}")
        assertEquals("hello", Encoded("hello").toStr())
    }

    @Test
    fun whatwgExamples() {
        assertEquals(
            "%%s%1G".encodeToByteArray().toList(),
            decodeBinary("%25%s%1G".encodeToByteArray()).toList(),
        )
        assertEquals(
            byteArrayOf(0xE2.toByte(), 0x80.toByte(), 0xBD.toByte(), 0x25, 0x2E).toList(),
            decodeBinary("‽%25%2E".encodeToByteArray()).toList(),
        )
        assertEquals("%E2%89%A1", encode("≡"))
        assertEquals("%E2%80%BD", encode("‽"))
        assertEquals("Say%20what%E2%80%BD", encode("Say what‽"))
    }
}
