package uk.matvey.lunatica.complaints

import com.neovisionaries.i18n.CountryCode
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.html.respondHtml
import io.ktor.server.request.receiveParameters
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import io.ktor.server.util.getOrFail
import kotlinx.html.body
import kotlinx.html.div
import kotlinx.html.id
import kotlinx.html.label
import kotlinx.html.p
import java.time.LocalDate
import java.util.UUID

fun Route.complaintRouting(complaintRepo: ComplaintRepo) {
    route("complaints") {
        post {
            val params = call.receiveParameters()
            val content = params.getOrFail("content")
            val complaint = Complaint.new(
                CountryCode.valueOf(params.getOrFail("problemCountry")),
                params["problemDate"]?.let(LocalDate::parse),
                params["type"]?.let(Complaint.Type::valueOf),
                mapOf("email" to params.getOrFail("email"))
            )
            complaintRepo.insert(complaint)
            call.respondHtml(HttpStatusCode.Created) {
                body {
                    p {
                        label {
                            +complaint.id.toString()
                        }
                    }
                    div {
                        id = "complaintContent"
                    }
                }
            }
        }
        get {
            val complaints = complaintRepo.list(20)
            call.respondHtml {
                body {
                    complaints.forEach { complaint ->
                        p {
                            +"${complaint.createdAt}"
                        }
                    }
                }
            }
        }
        route("/{id}") {
            get {
                val complaint = complaintRepo.get(UUID.fromString(call.parameters["id"]))
                call.respondHtml {
                    body {
                        p {
                            +"${complaint.createdAt}"
                        }
                    }
                }
            }
        }
    }
}
