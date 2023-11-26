package uk.matvey.lunatica.app

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm.HMAC256
import com.pengrad.telegrambot.TelegramBot
import io.ktor.http.HttpHeaders.AccessControlAllowHeaders
import io.ktor.http.HttpHeaders.AccessControlAllowMethods
import io.ktor.http.HttpHeaders.AccessControlAllowOrigin
import io.ktor.http.HttpHeaders.Authorization
import io.ktor.http.HttpHeaders.ContentType
import io.ktor.http.HttpHeaders.Cookie
import io.ktor.http.HttpHeaders.SetCookie
import io.ktor.http.HttpMethod.Companion.Delete
import io.ktor.http.HttpMethod.Companion.Options
import io.ktor.http.HttpMethod.Companion.Patch
import io.ktor.http.HttpMethod.Companion.Put
import io.ktor.http.HttpStatusCode.Companion.OK
import io.ktor.http.HttpStatusCode.Companion.Unauthorized
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.application.install
import io.ktor.server.auth.UserIdPrincipal
import io.ktor.server.auth.authentication
import io.ktor.server.auth.basic
import io.ktor.server.engine.ApplicationEngine
import io.ktor.server.engine.embeddedServer
import io.ktor.server.http.content.staticResources
import io.ktor.server.netty.Netty
import io.ktor.server.plugins.callloging.CallLogging
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.plugins.cors.routing.CORS
import io.ktor.server.response.respond
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import io.ktor.server.routing.routing
import io.ktor.util.date.GMTDate
import kotlinx.serialization.Serializable
import org.slf4j.event.Level
import uk.matvey.lunatica.account.accountRouting
import uk.matvey.lunatica.complaint.ComplaintSetup.JSON
import uk.matvey.lunatica.complaint.complaintRouting
import uk.matvey.lunatica.message.messageRouting
import uk.matvey.lunatica.sha256

fun createServer(
    services: Services,
    repos: Repos,
    bot: TelegramBot?,
): ApplicationEngine {
    return embeddedServer(Netty, port = 8080) {
        setupServer(services, repos, bot)
    }
}

fun Application.setupServer(
    services: Services,
    repos: Repos,
    bot: TelegramBot?,
) {
    install(CallLogging) {
        level = Level.INFO
    }
    install(ContentNegotiation) {
        json(JSON)
    }
    install(CORS) {
        allowMethod(Options)
        allowMethod(Put)
        allowMethod(Delete)
        allowMethod(Patch)

        allowHeader(AccessControlAllowHeaders)
        allowHeader(AccessControlAllowMethods)
        allowHeader(AccessControlAllowOrigin)
        allowHeader(ContentType)
        allowHeader(Authorization)
        allowHeader(Cookie)

        exposeHeader(SetCookie)

        allowCredentials = true

        anyHost()
    }
    authentication {
        basic {
            realm = "Ktor"
            validate { credentials ->
                repos.accountRepo.findByEmail(credentials.name)?.takeIf { account ->
                    account.passHash == sha256(credentials.password)
                }?.let { UserIdPrincipal(credentials.name) }
            }
        }
    }
    setupRouting(services, repos, bot)
}

fun Application.setupRouting(services: Services, repos: Repos, bot: TelegramBot?) {
    routing {
        staticResources("/", "public")
        get("/healthcheck") {
            call.respond(OK)
        }
        route("/api") {
            post("/login") { request: LoginRequest ->
                val account = repos.accountRepo.findByEmail(request.email)
                    ?.takeIf { sha256(request.pass) == it.passHash }
                    ?: return@post call.respond(Unauthorized)
                val token = JWT.create()
                    .withSubject(account.id.toString())
                    .withClaim("email", request.email)
                    .withClaim("roles", account.roles)
                    .sign(HMAC256("auth-secret"))
                call.response.cookies.append(
                    name = "auth",
                    value = token,
                    httpOnly = true,
                )
                call.respond(OK, token)
            }
            post("/logout") {
                call.response.cookies.append(
                    name = "auth",
                    value = "",
                    expires = GMTDate.START,
                )
                call.respond(OK)
            }
            infoRouting()
            accountRouting(services.accountService, repos.accountRepo)
            complaintRouting(services.complaintService, repos.complaintRepo, services.messageService, repos.accountRepo)
            messageRouting(services.messageService, repos.messageRepo, repos.accountRepo, repos.complaintRepo, bot)
        }
    }
}

@Serializable
data class LoginRequest(
    val email: String,
    val pass: String,
)
