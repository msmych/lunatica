package uk.matvey.lunatica.complaint

import com.neovisionaries.i18n.CountryCode
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDate
import java.util.UUID

@RestController
@RequestMapping("/complaints")
class ComplaintController(private val complaintService: ComplaintService) {
    @PostMapping
    fun createComplaint(@RequestBody request: CreateComplaintRequest): ResponseEntity<UUID> {
        val complaintId = complaintService.saveComplaint(
            UUID.randomUUID(), // TODO get account id from auth
            request.problemCountry,
            request.problemDate,
            request.type,
            request.content
        )
        return ResponseEntity(complaintId, HttpStatus.CREATED)
    }

    data class CreateComplaintRequest(
        val problemCountry: CountryCode,
        val problemDate: LocalDate,
        val type: Complaint.Type,
        val content: String
    )
}
