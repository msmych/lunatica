package uk.matvey.lunatica

import org.apache.commons.codec.digest.MessageDigestAlgorithms
import java.security.MessageDigest

fun sha256(s: String): String {
    val digest = MessageDigest.getInstance(MessageDigestAlgorithms.SHA_256)
    val hash = digest.digest(s.toByteArray())
    return hash.map { Integer.toHexString(0xff and it.toInt()) }
        .joinToString("") { if (it.length == 1) "0" else it }
}
