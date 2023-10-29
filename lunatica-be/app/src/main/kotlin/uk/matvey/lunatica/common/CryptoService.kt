package uk.matvey.lunatica.common

import org.apache.commons.codec.digest.MessageDigestAlgorithms.SHA_256
import org.springframework.stereotype.Service
import java.security.MessageDigest

@Service
class CryptoService {
    fun sha256(s: String): String {
        val digest = MessageDigest.getInstance(SHA_256)
        val hash = digest.digest(s.toByteArray())
        return hash.map { Integer.toHexString(0xff and it.toInt()) }
            .joinToString("") { if (it.length == 1) "0" else it }
    }
}
