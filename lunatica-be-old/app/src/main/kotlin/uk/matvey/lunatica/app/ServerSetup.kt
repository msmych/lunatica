package uk.matvey.lunatica.app

import io.ktor.http.HttpStatusCode.Companion.OK
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.application.install
import io.ktor.server.engine.ApplicationEngine
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.plugins.callloging.CallLogging
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.response.respond
import io.ktor.server.routing.get
import io.ktor.server.routing.route
import io.ktor.server.routing.routing
import org.slf4j.event.Level
import uk.matvey.lunatica.complaints.ComplaintRepo
import uk.matvey.lunatica.complaints.ComplaintSetup.JSON
import uk.matvey.lunatica.complaints.complaintRouting
import uk.matvey.lunatica.complaints.messages.MessageRepo

fun createServer(complaintRepo: ComplaintRepo, messageRepo: MessageRepo): ApplicationEngine {
    return embeddedServer(Netty, port = 8080) {
        setupServer(complaintRepo, messageRepo)
    }
}

fun Application.setupServer(complaintRepo: ComplaintRepo, messageRepo: MessageRepo) {
    install(CallLogging) {
        level = Level.INFO
    }
    install(ContentNegotiation) {
        json(JSON)
    }
    setupRouting(complaintRepo, messageRepo)
}

fun Application.setupRouting(complaintRepo: ComplaintRepo, messageRepo: MessageRepo) {
    routing {
        get("/healthcheck") {
            call.respond(OK)
        }
        route("/api") {
            complaintRouting(complaintRepo, messageRepo)
        }
    }
}
