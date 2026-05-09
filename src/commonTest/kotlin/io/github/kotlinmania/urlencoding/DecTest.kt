// port-lint: source src/dec.rs
package io.github.kotlinmania.urlencoding

import kotlin.test.Test
import kotlin.test.assertEquals

class DecTest {

    @Test
    fun decBorrows() {
        assertEquals("hello", decode("hello").getOrThrow())
        assertEquals("hello ", decode("hello%20").getOrThrow())
        assertEquals(" hello", decode("%20hello").getOrThrow())
    }
}
