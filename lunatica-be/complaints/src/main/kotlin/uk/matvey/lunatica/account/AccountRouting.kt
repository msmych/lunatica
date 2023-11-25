package uk.matvey.lunatica.account

import io.ktor.http.HttpStatusCode.Companion.Created
import io.ktor.server.application.call
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import kotlinx.serialization.Serializable

fun Route.accountRouting(accountService: AccountService) {
    route("/accounts") {
        post { request: CreateAccountRequest ->
            val accountId = accountService.createAccount(request.email, request.pass)
            call.respond(Created, """{"id":"$accountId"}""")
        }
    }
}

@Serializable
data class CreateAccountRequest(
    val email: String,
    val pass: String,
)
