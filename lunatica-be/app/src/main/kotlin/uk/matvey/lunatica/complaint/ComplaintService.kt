package uk.matvey.lunatica.complaint

import com.neovisionaries.i18n.CountryCode
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.util.UUID

@Service
class ComplaintService(private val complaintRepo: ComplaintRepo) {
    fun saveComplaint(
        accountId: UUID,
        problemCountry: CountryCode,
        problemDate: LocalDate,
        type: Complaint.Type,
        content: String
    ): UUID {
        val complaint = Complaint.new(accountId, problemCountry, problemDate, type)
        complaintRepo.add(complaint)
        // TODO create message as well
        return complaint.id
    }
}
