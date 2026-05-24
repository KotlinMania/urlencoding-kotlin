// port-lint: source lib.rs
package io.github.kotlinmania.urlencoding

/**
 * To encode a string, do the following:
 *
 * ```kotlin
 * import io.github.kotlinmania.urlencoding.encode
 *
 * val encoded = encode("This string will be URL encoded.")
 * println(encoded)
 * // This%20string%20will%20be%20URL%20encoded.
 * ```
 *
 * To decode a string, it's only slightly different:
 *
 * ```kotlin
 * import io.github.kotlinmania.urlencoding.decode
 *
 * val decoded = decode("%F0%9F%91%BE%20Exterminate%21").getOrThrow()
 * println(decoded)
 * // 👾 Exterminate!
 * ```
 *
 * To decode allowing arbitrary bytes and invalid UTF-8:
 *
 * ```kotlin
 * import io.github.kotlinmania.urlencoding.decodeBinary
 *
 * val binary = decodeBinary("%F1%F2%F3%C0%C1%C2".encodeToByteArray())
 * val decoded = binary.decodeToString()
 * ```
 *
 * Where the upstream returns its own borrowed/owned distinction to avoid allocating
 * when decoding or encoding is not needed, this Kotlin port returns plain `String`
 * and `ByteArray`. `decodeBinary` still returns the same `ByteArray` reference when
 * the input contains no `%`.
 */

// Crate-root export ledger: upstream exposes encode, encodeBinary, Encoded,
// decode, and decodeBinary from its encoding and decoding modules. Kotlin keeps
// those declarations at this package root without bridge aliases.
