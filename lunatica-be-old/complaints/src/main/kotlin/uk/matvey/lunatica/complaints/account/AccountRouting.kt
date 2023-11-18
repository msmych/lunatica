package uk.matvey.lunatica.complaints.account

import io.ktor.http.HttpStatusCode.Companion.Created
import io.ktor.server.application.call
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import kotlinx.serialization.Serializable
import uk.matvey.lunatica.sha256

fun Route.accountRouting(accountRepo: AccountRepo) {
    route("/accounts") {
        post { request: CreateAccountRequest ->
            val account = Account.account(request.email, sha256(request.pass))
            accountRepo.insert(account)
            call.respond(Created, """{"id":"${account.id}"}""")
        }
    }
}

@Serializable
data class CreateAccountRequest(
    val email: String,
    val pass: String,
)
