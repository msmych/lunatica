package uk.matvey.lunatica.message

import io.ktor.http.HttpStatusCode.Companion.OK
import io.ktor.server.application.call
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.route
import io.ktor.server.util.getOrFail
import java.util.UUID

fun Route.messageRouting(messageRepo: MessageRepo) {
    route("/messages") {
        get {
            val complaintId = UUID.fromString(call.request.queryParameters.getOrFail("complaintId"))
            val messages = messageRepo.listByComplaintId(complaintId)
            call.respond(OK, messages)
        }
    }
}
