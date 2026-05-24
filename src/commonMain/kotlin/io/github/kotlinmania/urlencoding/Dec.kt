// port-lint: source dec.rs
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

    val out = NeverRealloc(ByteArray(data.size))
    out.extendFromSlice(data, 0, offset)

    var d = offset
    while (true) {
        var nextPct = d
        while (nextPct < data.size && data[nextPct] != pct) nextPct++
        out.extendFromSlice(data, d, nextPct)
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
                    out.push(((firstVal shl 4) or secondVal).toByte())
                    d = restStart + 2
                } else {
                    out.push(pct)
                    out.push(first)
                    d = restStart + 1
                }
            } else {
                out.push(pct)
                d = restStart
            }
        } else {
            out.push(pct)
            out.extendFromSlice(data, restStart, data.size)
            break
        }
    }

    return out.toByteArray()
}

private class NeverRealloc(private val bytes: ByteArray) {
    var size: Int = 0
        private set

    fun push(value: Byte) {
        // These branches only exist to remove redundant reallocation work:
        // the capacity is always sufficient for decoded percent data.
        if (size != bytes.size) {
            bytes[size] = value
            size += 1
        }
    }

    fun extendFromSlice(value: ByteArray, startIndex: Int = 0, endIndex: Int = value.size) {
        val count = endIndex - startIndex
        if (bytes.size - size >= count) {
            value.copyInto(bytes, destinationOffset = size, startIndex = startIndex, endIndex = endIndex)
            size += count
        }
    }

    fun toByteArray(): ByteArray = bytes.copyOf(size)
}
