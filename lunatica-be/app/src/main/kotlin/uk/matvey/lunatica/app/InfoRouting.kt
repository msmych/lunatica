package uk.matvey.lunatica.app

import io.ktor.server.application.call
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import uk.matvey.lunatica.complaint.ComplaintSetup.COMPLAINTS_STATES
import uk.matvey.lunatica.complaint.ComplaintSetup.COMPLAINTS_TYPES
import uk.matvey.lunatica.complaint.ComplaintSetup.PROBLEM_COUNTRIES
import uk.matvey.lunatica.info.AppInfo
import uk.matvey.lunatica.info.AppInfo.ComplaintStateInfo
import uk.matvey.lunatica.info.AppInfo.ComplaintTypeInfo
import uk.matvey.lunatica.info.AppInfo.CountryInfo

fun Route.infoRouting() {
    get("/info") {
        val info = AppInfo(
            PROBLEM_COUNTRIES.map { (code, label) -> CountryInfo(code, label.emoji, label.nameRu) },
            COMPLAINTS_TYPES.map { (code, label) -> ComplaintTypeInfo(code, label.emoji, label.nameRu) },
            COMPLAINTS_STATES.map { (code, label) -> ComplaintStateInfo(code, label.emoji, label.nameRu) },
        )
        call.respond(info)
    }
}
