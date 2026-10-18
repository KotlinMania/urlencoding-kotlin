// port-lint: tests enc.rs
package io.github.kotlinmania.urlencoding.enc

import kotlin.test.Test
import kotlin.test.assertEquals

class EncTest {
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
    fun lazyWriter() {
        val s = StringBuilder("he")
        Encoded("llo").appendTo(s)
        assertEquals("hello", s.toString())

        assertEquals("hello", Encoded("hello").toString())
        assertEquals("hello", "${Encoded("hello")}")
        assertEquals("hello", Encoded("hello").toStr())

        val fmtBuffer = StringBuilder()
        Encoded("world").fmt(fmtBuffer)
        assertEquals("world", fmtBuffer.toString())
    }
}
