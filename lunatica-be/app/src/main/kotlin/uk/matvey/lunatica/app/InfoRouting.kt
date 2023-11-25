package uk.matvey.lunatica.app

import com.neovisionaries.i18n.CountryCode
import io.ktor.server.application.call
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import kotlinx.serialization.Serializable
import uk.matvey.lunatica.complaint.Complaint
import uk.matvey.lunatica.complaint.ComplaintSetup.COMPLAINTS_STATES
import uk.matvey.lunatica.complaint.ComplaintSetup.COMPLAINTS_TYPES
import uk.matvey.lunatica.complaint.ComplaintSetup.PROBLEM_COUNTRIES
import uk.matvey.lunatica.complaint.ComplaintSetup.TgLabel

fun Route.infoRouting() {
    get("/info") {
        val info = AppInfo(
            PROBLEM_COUNTRIES,
            COMPLAINTS_TYPES,
            COMPLAINTS_STATES,
        )
        call.respond(info)
    }
}

@Serializable
data class AppInfo(
    val countries: Map<CountryCode, TgLabel>,
    val complaintTypes: Map<Complaint.Type, TgLabel>,
    val complaintStates: Map<Complaint.State, TgLabel>,
)
