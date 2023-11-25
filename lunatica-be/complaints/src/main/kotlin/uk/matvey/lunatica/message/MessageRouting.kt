package uk.matvey.lunatica.message

import com.auth0.jwt.JWT
import io.ktor.http.HttpStatusCode.Companion.Created
import io.ktor.http.HttpStatusCode.Companion.Unauthorized
import io.ktor.server.application.call
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import io.ktor.server.util.getOrFail
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.util.UUID

fun Route.messageRouting(messageService: MessageService, messageRepo: MessageRepo) {
    route("/messages") {
        get {
            val complaintId = UUID.fromString(call.request.queryParameters.getOrFail("complaintId"))
            val messages = messageRepo.listByComplaintId(complaintId)
            call.respond(Created, messages)
        }
        post { request: CreateMessageRequest ->
            val token = call.request.cookies["auth"] ?: return@post call.respond(Unauthorized)
            val accountId = UUID.fromString(JWT.decode(token).subject)
            val message = messageService.createMessage(accountId, request.complaintId, request.content)
            call.respond(Created, """{"id":"${message.id}"}""")
        }
    }
}

@Serializable
data class CreateMessageRequest(
    val complaintId: @Contextual UUID,
    val content: String,
)
