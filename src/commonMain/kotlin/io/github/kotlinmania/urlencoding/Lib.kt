// port-lint: source src/lib.rs
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
 * when decoding/encoding is not needed, this Kotlin port returns plain `String` and
 * `ByteArray`. `decodeBinary` still returns the same `ByteArray` reference when the
 * input contains no `%`.
 */

// Re-export ledger (translation tracking; symbols already live at this package root).
//
// pub use enc::encode;
// pub use enc::encode_binary;
// pub use enc::Encoded;
// pub use dec::decode;
// pub use dec::decode_binary;
