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

fun Route.infoRouting() {
    get("/info") {
        val info = AppInfo(
            PROBLEM_COUNTRIES.map { (code, label) -> AppInfo.CountryInfo(code, label.emoji, label.nameRu) },
            COMPLAINTS_TYPES.map { (code, label) -> AppInfo.ComplaintTypeInfo(code, label.emoji, label.nameRu) },
            COMPLAINTS_STATES.map { (code, label) -> AppInfo.ComplaintStateInfo(code, label.emoji, label.nameRu) },
        )
        call.respond(info)
    }
}

@Serializable
data class AppInfo(
    val countries: List<CountryInfo>,
    val complaintTypes: List<ComplaintTypeInfo>,
    val complaintStates: List<ComplaintStateInfo>,
) {

    @Serializable
    data class CountryInfo(
        val code: CountryCode,
        val emoji: String,
        val nameRu: String,
    )

    @Serializable
    data class ComplaintTypeInfo(
        val code: Complaint.Type,
        val emoji: String,
        val nameRu: String,
    )

    @Serializable
    data class ComplaintStateInfo(
        val code: Complaint.State,
        val emoji: String,
        val nameRu: String,
    )
}
