package uk.matvey.lunatica.complaint

import com.neovisionaries.i18n.CountryCode
import org.springframework.stereotype.Service
import uk.matvey.lunatica.message.Message
import uk.matvey.lunatica.message.MessageRepo
import java.time.LocalDate
import java.util.UUID

@Service
class ComplaintService(
    private val complaintRepo: ComplaintRepo,
    private val messageRepo: MessageRepo,
) {

    fun saveComplaint(
        accountId: UUID,
        problemCountry: CountryCode,
        problemDate: LocalDate,
        type: Complaint.Type,
        content: String
    ): UUID {
        val complaint = Complaint.new(accountId, problemCountry, problemDate, type)
        complaintRepo.add(complaint)
        val message = Message.complaintMessage(complaint.id, content)
        messageRepo.add(message)
        return complaint.id
    }
}
