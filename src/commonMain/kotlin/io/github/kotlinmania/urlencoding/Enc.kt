// port-lint: source enc.rs
package io.github.kotlinmania.urlencoding

/**
 * Wrapper type that implements `toString`. Encodes on the fly, without allocating.
 * Percent-encodes every byte except alphanumerics and `-`, `_`, `.`, `~`. Assumes UTF-8 encoding.
 *
 * ```kotlin
 * import io.github.kotlinmania.urlencoding.Encoded
 * "${Encoded("hello!")}"
 * ```
 *
 * The wrapped value must be a `String` or a `ByteArray`.
 */
internal data class Encoded<T>(val value: T) {

    /**
     * Returns the percent-encoded form of the wrapped value.
     */
    fun toStr(): String = encodeBinary(asBytes(value))

    /**
     * Performs urlencoding to a string.
     */
    override fun toString(): String = toStr()

    /**
     * Performs urlencoding into a writer.
     */
    fun write(writer: (String) -> Unit) {
        encodeInto(asBytes(value), false) { s -> writer(s) }
    }

    /**
     * Performs urlencoding into a string.
     */
    fun appendTo(string: StringBuilder) {
        appendString(asBytes(value), string, false)
    }

    companion object {
        /**
         * Long way of writing `Encoded(data)`.
         *
         * Takes any string-like type or a byte array.
         */
        fun <T> new(string: T): Encoded<T> = Encoded(string)

        /**
         * Same as `new`, but hints a more specific type.
         */
        fun str(string: String): Encoded<String> = Encoded(string)
    }
}

private fun asBytes(value: Any?): ByteArray = when (value) {
    is String -> value.encodeToByteArray()
    is ByteArray -> value
    else -> throw IllegalArgumentException(
        "Encoded value must be String or ByteArray, got ${value?.let { it::class }}"
    )
}

/**
 * Percent-encodes every byte except alphanumerics and `-`, `_`, `.`, `~`. Assumes UTF-8 encoding.
 */
fun encode(data: String): String = encodeBinary(data.encodeToByteArray())

/**
 * Percent-encodes every byte except alphanumerics and `-`, `_`, `.`, `~`.
 */
fun encodeBinary(data: ByteArray): String {
    val escaped = StringBuilder(maxOf(data.size, 15))
    val unmodified = appendString(data, escaped, true)
    if (unmodified) {
        return data.decodeToString()
    }
    return escaped.toString()
}

private fun appendString(data: ByteArray, escaped: StringBuilder, maySkip: Boolean): Boolean =
    encodeInto(data, maySkip) { s -> escaped.append(s) }

private inline fun encodeInto(
    data: ByteArray,
    maySkipWrite: Boolean,
    pushStr: (String) -> Unit,
): Boolean {
    var off = 0
    var pushed = false
    while (true) {
        var asciiLen = 0
        while (off + asciiLen < data.size) {
            val c = data[off + asciiLen].toInt() and 0xFF
            val safe = (c in '0'.code..'9'.code) ||
                (c in 'A'.code..'Z'.code) ||
                (c in 'a'.code..'z'.code) ||
                c == '-'.code || c == '.'.code || c == '_'.code || c == '~'.code
            if (!safe) break
            asciiLen++
        }
        val safeEnd = off + asciiLen
        val noUnsafeRest = safeEnd >= data.size
        if (noUnsafeRest && !pushed && maySkipWrite) {
            return true
        }
        pushed = true
        if (asciiLen > 0) {
            pushStr(data.decodeToString(off, safeEnd))
        }
        if (noUnsafeRest) {
            break
        }
        val byte = data[safeEnd].toInt() and 0xFF
        val enc = charArrayOf('%', toHexDigit(byte shr 4), toHexDigit(byte and 15))
        pushStr(enc.concatToString())
        off = safeEnd + 1
    }
    return false
}

private fun toHexDigit(digit: Int): Char = when (digit) {
    in 0..9 -> ('0'.code + digit).toChar()
    else -> ('A'.code - 10 + digit).toChar()
}
