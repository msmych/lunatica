package uk.matvey.lunatica.app

import io.ktor.http.HttpStatusCode.Companion.OK
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.application.install
import io.ktor.server.engine.ApplicationEngine
import io.ktor.server.engine.embeddedServer
import io.ktor.server.html.respondHtml
import io.ktor.server.netty.Netty
import io.ktor.server.plugins.callloging.CallLogging
import io.ktor.server.response.respond
import io.ktor.server.routing.get
import io.ktor.server.routing.route
import io.ktor.server.routing.routing
import org.slf4j.event.Level
import uk.matvey.lunatica.complaints.ComplaintRepo
import uk.matvey.lunatica.complaints.complaintRouting

fun createServer(complaintRepo: ComplaintRepo): ApplicationEngine {
    return embeddedServer(Netty, port = 8080) {
        setupServer(complaintRepo)
    }
}

fun Application.setupServer(complaintPgRepo: ComplaintRepo) {
    install(CallLogging) {
        level = Level.INFO
    }
    setupRouting(complaintPgRepo)
}

fun Application.setupRouting(complaintRepo: ComplaintRepo) {
    routing {
        get("/healthcheck") {
            call.respond(OK)
        }
        get {
            call.respondHtml(block = { index() })
        }
        route("/api") {
            complaintRouting(complaintRepo)
        }
    }
}
