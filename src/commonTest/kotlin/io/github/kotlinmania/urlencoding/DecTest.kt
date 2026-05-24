// port-lint: source dec.rs
package io.github.kotlinmania.urlencoding

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class DecTest {

    @Test
    fun decBorrows() {
        val plain = "hello".encodeToByteArray()
        assertTrue(decodeBinary(plain) === plain)

        val trailingInput = "hello%20".encodeToByteArray()
        val trailing = decodeBinary(trailingInput)
        assertTrue(trailing !== trailingInput)
        assertEquals("hello ", trailing.decodeToString())

        val leading = decodeBinary("%20hello".encodeToByteArray())
        assertEquals(" hello", leading.decodeToString())
    }
}
