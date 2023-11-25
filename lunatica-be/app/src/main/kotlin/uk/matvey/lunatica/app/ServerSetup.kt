package uk.matvey.lunatica.app

import io.ktor.http.HttpStatusCode.Companion.OK
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
import io.ktor.server.response.respond
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import io.ktor.server.routing.routing
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
): ApplicationEngine {
    return embeddedServer(Netty, port = 8080) {
        setupServer(services, repos)
    }
}

fun Application.setupServer(
    services: Services,
    repos: Repos,
) {
    install(CallLogging) {
        level = Level.INFO
    }
    install(ContentNegotiation) {
        json(JSON)
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
    setupRouting(services, repos)
}

fun Application.setupRouting(services: Services, repos: Repos) {
    routing {
        staticResources("/", "public")
        get("/healthcheck") {
            call.respond(OK)
        }
        route("/api") {
            post("/login") { request: LoginRequest ->

            }
            accountRouting(services.accountService)
            complaintRouting(repos.complaintRepo, repos.messageRepo)
            messageRouting(repos.messageRepo)
        }
    }
}

@Serializable
data class LoginRequest(
    val email: String,
    val pass: String,
)
