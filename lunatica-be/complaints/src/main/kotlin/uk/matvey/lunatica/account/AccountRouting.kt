package uk.matvey.lunatica.account

import io.ktor.http.HttpStatusCode.Companion.Created
import io.ktor.server.application.call
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import io.ktor.server.util.getOrFail
import kotlinx.serialization.Serializable
import java.util.UUID

fun Route.accountRouting(accountService: AccountService, accountPgRepo: AccountPgRepo) {
    route("/accounts") {
        get("/collaborators") {
            val collaborators = accountPgRepo.getCollaborators()
            call.respond(collaborators)
        }
        post { request: CreateAccountRequest ->
            val accountId = accountService.createAccount(request.email, request.pass)
            call.respond(Created, """{"id":"$accountId"}""")
        }
        route("{id}") {
            get {
                val account = accountPgRepo.get(UUID.fromString(call.parameters.getOrFail("id")))
                call.respond(account)
            }
        }
    }
}

@Serializable
data class CreateAccountRequest(
    val email: String,
    val pass: String,
)
