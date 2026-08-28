// port-lint: tests benches/bench.rs
package io.github.kotlinmania.urlencoding

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class BenchTest {
    @Test
    fun benchEncNopShort() {
        assertEquals("hello", encode("hello"))
    }

    @Test
    fun benchEncNopLong() {
        val input = "Lorem-ipsum-dolor-sit-amet-consectetur-adipisicing-elit-sed-do-eiusmod-tempor-incididunt-ut-labore-et-dolore-magna-aliqua.Ut-enim-ad-minim-veniam-quis-nostrud" +
            "-exercitation-ullamco-laboris-nisi-ut-aliquip-ex-ea-commodo-consequat.Duis-aute-irure-dolor-in-reprehenderit-in-voluptate-velit-esse-cillum-dolore-eu-fugiat-nulla" +
            "-pariatur.Excepteur-sint-occaecat-cupidatat-non-proident-sunt-in-culpa-qui-officia-deserunt-mollit-anim-id-est-laborum."
        assertEquals(input, encode(input))
    }

    @Test
    fun benchDecNopShort() {
        assertEquals("hello", decode("hello").getOrThrow())
    }

    @Test
    fun benchDecNopLong() {
        val input = "Lorem-ipsum-dolor-sit-amet-consectetur-adipisicing-elit-sed-do-eiusmod-tempor-incididunt-ut-labore-et-dolore-magna-aliqua.Ut-enim-ad-minim-veniam-quis-nostrud" +
            "-exercitation-ullamco-laboris-nisi-ut-aliquip-ex-ea-commodo-consequat.Duis-aute-irure-dolor-in-reprehenderit-in-voluptate-velit-esse-cillum-dolore-eu-fugiat-nulla" +
            "-pariatur.Excepteur-sint-occaecat-cupidatat-non-proident-sunt-in-culpa-qui-officia-deserunt-mollit-anim-id-est-laborum."
        assertEquals(input, decode(input).getOrThrow())
    }

    @Test
    fun benchEncChgShort() {
        assertEquals("he%21%21o", encode("he!!o"))
    }

    @Test
    fun benchEncChgLong() {
        val input = "Lorem ipsum dolor sit amet consectetur adipisicing elit sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.Ut enim ad minim veniam quis nostrud" +
            " exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla" +
            " pariatur. Excepteur sint occaecat cupidatat non proident sunt in culpa qui officia deserunt mollit anim id est laborum."
        assertTrue(encode(input).contains("%20"))
    }

    @Test
    fun benchDecChgShort() {
        assertEquals("he&&o", decode("he%26%26o").getOrThrow())
    }

    @Test
    fun benchDecChgLong() {
        val input = "Lorem%20ipsum%20dolor%20sit%20amet%20consectetur%20adipisicing%20elit%20sed%20do%20eiusmod%20tempor%20incididunt%20ut%20labore%20et%20dolore%20magna%20aliqua.Ut%20enim%20ad%20minim%20veniam%20quis%20nostrud" +
            "%20exercitation%20ullamco%20laboris%20nisi%20ut%20aliquip%20ex%20ea%20commodo%20consequat.Duis%20aute%20irure%20dolor%20in%20reprehenderit%20in%20voluptate%20velit%20esse%20cillum%20dolore%20eu%20fugiat%20nulla" +
            "%20pariatur.Excepteur%20sint%20occaecat%20cupidatat%20non%20proident%20sunt%20in%20culpa%20qui%20officia%20deserunt%20mollit%20anim%20id%20est%20laborum."
        assertTrue(decode(input).getOrThrow().contains(" "))
    }
}
