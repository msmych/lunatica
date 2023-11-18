package uk.matvey.lunatica.complaints.account

import io.ktor.http.HttpStatusCode.Companion.Created
import io.ktor.server.application.call
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import kotlinx.serialization.Serializable
import org.apache.commons.codec.digest.MessageDigestAlgorithms.SHA_256
import java.security.MessageDigest

fun Route.accountRouting(accountRepo: AccountRepo) {
    route("/accounts") {
        post { request: CreateAccountRequest ->
            val account = Account.account(request.email, sha256(request.pass))
            accountRepo.insert(account)
            call.respond(Created, """{"id":"${account.id}"}""")
        }
    }
}

fun sha256(s: String): String {
    val digest = MessageDigest.getInstance(SHA_256)
    val hash = digest.digest(s.toByteArray())
    return hash.map { Integer.toHexString(0xff and it.toInt()) }
        .joinToString("") { if (it.length == 1) "0" else it }
}

@Serializable
data class CreateAccountRequest(
    val email: String,
    val pass: String,
)
