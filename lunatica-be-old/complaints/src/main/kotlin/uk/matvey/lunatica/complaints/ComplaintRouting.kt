package uk.matvey.lunatica.complaints

import com.neovisionaries.i18n.CountryCode
import io.ktor.http.HttpStatusCode.Companion.Created
import io.ktor.http.HttpStatusCode.Companion.OK
import io.ktor.server.application.call
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import uk.matvey.lunatica.complaints.messages.Message
import uk.matvey.lunatica.complaints.messages.MessageRepo
import java.time.LocalDate
import java.util.UUID
import java.util.UUID.randomUUID

fun Route.complaintRouting(complaintRepo: ComplaintRepo, messageRepo: MessageRepo) {
    route("complaints") {
        post { request: CreateComplaintRequest ->
            val complaint = Complaint.new(
                randomUUID(),
                request.problemCountry,
                request.problemDate,
                request.type
            )
            complaintRepo.insert(complaint)
            messageRepo.insert(Message.complaintMessage(complaint.id, request.content))
            call.respond(Created, """{"id":"${complaint.id}"}""")
        }
        route("/{id}") {
            get {
                val complaint = complaintRepo.get(UUID.fromString(call.parameters["id"]))
                call.respond(OK, complaint)
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
