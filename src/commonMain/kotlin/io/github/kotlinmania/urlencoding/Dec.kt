// port-lint: source src/dec.rs
package io.github.kotlinmania.urlencoding

internal fun fromHexDigit(digit: Byte): Int? {
    val c = digit.toInt() and 0xFF
    return when (c) {
        in '0'.code..'9'.code -> c - '0'.code
        in 'A'.code..'F'.code -> c - 'A'.code + 10
        in 'a'.code..'f'.code -> c - 'a'.code + 10
        else -> null
    }
}

/**
 * Decodes a percent-encoded string assuming UTF-8 encoding.
 *
 * Unencoded `+` is preserved literally, and _not_ changed to a space.
 *
 * Returns a failed `Result` if the decoded bytes are not valid UTF-8.
 */
fun decode(data: String): Result<String> {
    val bytes = data.encodeToByteArray()
    val decoded = decodeBinary(bytes)
    if (decoded === bytes) {
        return Result.success(data)
    }
    return runCatching { decoded.decodeToString(throwOnInvalidSequence = true) }
}

/**
 * Decodes a percent-encoded byte string as binary data, in any encoding.
 *
 * Unencoded `+` is preserved literally, and _not_ changed to a space.
 *
 * If the input contains no `%`, the same `ByteArray` reference is returned.
 */
fun decodeBinary(data: ByteArray): ByteArray {
    val pct = '%'.code.toByte()
    var offset = 0
    while (offset < data.size && data[offset] != pct) offset++
    if (offset >= data.size) {
        return data
    }

    val decoded = ByteArray(data.size)
    var outLen = 0
    for (i in 0 until offset) {
        decoded[outLen++] = data[i]
    }

    var d = offset
    while (true) {
        var nextPct = d
        while (nextPct < data.size && data[nextPct] != pct) nextPct++
        for (i in d until nextPct) {
            decoded[outLen++] = data[i]
        }
        if (nextPct >= data.size) {
            break
        }

        val restStart = nextPct + 1
        val restLen = data.size - restStart
        if (restLen >= 2) {
            val first = data[restStart]
            val second = data[restStart + 1]
            val firstVal = fromHexDigit(first)
            if (firstVal != null) {
                val secondVal = fromHexDigit(second)
                if (secondVal != null) {
                    decoded[outLen++] = ((firstVal shl 4) or secondVal).toByte()
                    d = restStart + 2
                } else {
                    decoded[outLen++] = pct
                    decoded[outLen++] = first
                    d = restStart + 1
                }
            } else {
                decoded[outLen++] = pct
                d = restStart
            }
        } else {
            decoded[outLen++] = pct
            for (i in restStart until data.size) {
                decoded[outLen++] = data[i]
            }
            break
        }
    }

    return decoded.copyOf(outLen)
}
