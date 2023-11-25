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
import uk.matvey.lunatica.account.Account
import uk.matvey.lunatica.account.AccountPgRepo
import uk.matvey.lunatica.complaint.ComplaintSetup.COMPLAINTS_STATES
import uk.matvey.lunatica.complaint.ComplaintSetup.COMPLAINTS_TYPES
import uk.matvey.lunatica.complaint.ComplaintSetup.PROBLEM_COUNTRIES
import uk.matvey.lunatica.info.AppInfo.ComplaintStateInfo
import uk.matvey.lunatica.info.AppInfo.ComplaintTypeInfo
import uk.matvey.lunatica.info.AppInfo.CountryInfo
import uk.matvey.lunatica.message.MessageService
import java.time.Instant
import java.time.LocalDate
import java.util.UUID

fun Route.complaintRouting(complaintRepo: ComplaintRepo, messageService: MessageService, accountPgRepo: AccountPgRepo) {
    route("complaints") {
        get {
            val limit = call.request.queryParameters["limit"]?.toInt() ?: 20
            val complaints = complaintRepo.list(limit)
                .map { complaint ->
                    ComplaintResponseItem(
                        complaint.id,
                        complaint.accountId,
                        accountPgRepo.get(complaint.accountId),
                        complaint.state.let {
                            ComplaintStateInfo(
                                it,
                                COMPLAINTS_STATES.getValue(it).emoji,
                                COMPLAINTS_STATES.getValue(it).nameRu
                            )
                        },
                        complaint.problemCountry?.let {
                            CountryInfo(
                                it,
                                PROBLEM_COUNTRIES.getValue(it).emoji,
                                PROBLEM_COUNTRIES.getValue(it).nameRu
                            )
                        },
                        complaint.problemDate,
                        complaint.type?.let {
                            ComplaintTypeInfo(
                                it,
                                COMPLAINTS_TYPES.getValue(it).emoji,
                                COMPLAINTS_TYPES.getValue(it).nameRu
                            )
                        },
                        complaint.createdAt,
                        complaint.updatedAt,
                    )
                }
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

@Serializable
data class ComplaintResponseItem(
    val id: @Contextual UUID,
    val accountId: @Contextual UUID,
    val account: Account,
    val state: ComplaintStateInfo,
    val problemCountry: CountryInfo?,
    val problemDate: @Contextual LocalDate?,
    val type: ComplaintTypeInfo?,
    val createdAt: @Contextual Instant,
    val updatedAt: @Contextual Instant
)
