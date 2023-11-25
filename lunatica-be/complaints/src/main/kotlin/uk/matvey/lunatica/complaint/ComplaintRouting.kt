package uk.matvey.lunatica.complaint

import com.auth0.jwt.JWT
import com.neovisionaries.i18n.CountryCode
import io.ktor.http.HttpStatusCode
import io.ktor.http.HttpStatusCode.Companion.Created
import io.ktor.http.HttpStatusCode.Companion.NoContent
import io.ktor.server.application.call
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.patch
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import uk.matvey.lunatica.message.MessageService
import java.time.LocalDate
import java.util.UUID

fun Route.complaintRouting(complaintRepo: ComplaintRepo, messageService: MessageService) {
    route("complaints") {
        get {
            val limit = call.request.queryParameters["limit"]?.toInt() ?: 20
            val complaints = complaintRepo.list(limit)
            call.respond(complaints)
        }
        post { request: CreateComplaintRequest ->
            val token = call.request.cookies["auth"] ?: return@post call.respond(HttpStatusCode.Unauthorized)
            val accountId = UUID.fromString(JWT.decode(token).subject)
            val complaint = Complaint.new(
                accountId,
                request.problemCountry,
                request.problemDate,
                request.type
            )
            complaintRepo.insert(complaint)
            messageService.createMessage(accountId, complaint.id, request.content)
            call.respond(Created, """{"id":"${complaint.id}"}""")
        }
        route("/{id}") {
            get {
                val complaint = complaintRepo.get(UUID.fromString(call.parameters["id"]))
                call.respond(complaint)
            }
            patch { request: UpdateComplaintRequest ->
                val complaint = complaintRepo.get(UUID.fromString(call.parameters["id"]))
                complaintRepo.update(complaint.copy(state = request.state))
                call.respond(NoContent)
            }
        }
    }
}

@Serializable
data class CreateComplaintRequest(
    val problemCountry: CountryCode,
    val problemDate: @Contextual LocalDate,
    val type: Complaint.Type,
    val content: String,
)

@Serializable
data class UpdateComplaintRequest(
    val state: Complaint.State,
)
