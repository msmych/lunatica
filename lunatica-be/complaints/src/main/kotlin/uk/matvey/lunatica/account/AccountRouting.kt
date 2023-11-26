package uk.matvey.lunatica.account

import com.auth0.jwt.JWT
import io.ktor.http.HttpStatusCode.Companion.Created
import io.ktor.http.HttpStatusCode.Companion.OK
import io.ktor.http.HttpStatusCode.Companion.Unauthorized
import io.ktor.server.application.call
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.patch
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import io.ktor.server.util.getOrFail
import kotlinx.serialization.Serializable
import java.util.UUID

fun Route.accountRouting(accountService: AccountService, accountRepo: AccountRepo) {
    route("/accounts") {
        get {
            val role = call.request.queryParameters["role"]?.let(Account.Role::valueOf)
            val accounts = accountRepo.list(role)
            call.respond(accounts)
        }
        post { request: CreateAccountRequest ->
            val accountId = request.email.let {
                accountRepo.findByEmail(it)?.let { account ->
                    accountService.updateAccount(account.id, null, request.pass)
                    account.id
                }
            }
                ?: accountService.createAccount(request.email, request.pass, request.name)
            call.respond(Created, """{"id":"$accountId"}""")
        }
        route("{id}") {
            get {
                val account = accountRepo.get(UUID.fromString(call.parameters.getOrFail("id")))
                call.respond(account)
            }
            route("roles/{role}") {
                post("/toggle") {
                    val role = Account.Role.valueOf(call.parameters.getOrFail("role"))
                    val accountId = UUID.fromString(call.parameters.getOrFail("id"))
                    accountService.toggleAccountRole(accountId, role)
                }
            }
        }
    }
    route("/me") {
        get {
            val token = call.request.cookies["auth"] ?: return@get call.respond(Unauthorized)
            val decoded = JWT.decode(token)
            val account = accountRepo.get(UUID.fromString(decoded.subject))
            call.respond(account)
        }
        patch { request: UpdateMeRequest ->
            val token = call.request.cookies["auth"] ?: return@patch call.respond(Unauthorized)
            val decoded = JWT.decode(token)
            accountService.updateAccount(UUID.fromString(decoded.subject), request.name, request.pass)
            call.respond(OK)
        }
    }
}

@Serializable
data class CreateAccountRequest(
    val email: String,
    val pass: String,
    val name: String?,
)

@Serializable
data class UpdateMeRequest(
    val name: String?,
    val pass: String?,
)
