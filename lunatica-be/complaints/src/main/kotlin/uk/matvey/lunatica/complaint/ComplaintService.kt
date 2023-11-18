package uk.matvey.lunatica.complaint

import com.neovisionaries.i18n.CountryCode
import java.util.UUID

class ComplaintService(private val complaintRepo: ComplaintRepo) {

    suspend fun createDraftComplaint(accountId: UUID): Complaint {
        val complaint = Complaint.draft(accountId)
        complaintRepo.insert(complaint)
        return complaint
    }

    suspend fun updateComplaintState(complaint: Complaint, state: Complaint.State) {
        complaintRepo.update(complaint.copy(state = state))
    }

    suspend fun updateComplaintProblemCountry(complaint: Complaint, country: CountryCode) {
        complaintRepo.update(
            complaint.copy(problemCountry = country)
        )
    }

    suspend fun updateComplaintType(complaint: Complaint, type: Complaint.Type) {
        complaintRepo.update(
            complaint.copy(type = type)
        )
    }
}
