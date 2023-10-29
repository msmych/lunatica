package uk.matvey.lunatica.complaint

import com.google.cloud.firestore.Firestore
import com.neovisionaries.i18n.CountryCode
import org.springframework.stereotype.Component
import java.time.Instant
import java.time.LocalDate
import java.util.UUID

@Component
class ComplaintRepo(private val db: Firestore) {
    fun add(complaint: Complaint) {
        db.collection("complaints").document(complaint.id.toString()).set(complaint.toDoc())
    }
    fun get(id: UUID): Complaint {
        val doc = db.collection("complaints").document(id.toString()).get().get()
        return fromDoc(id, requireNotNull(doc.data))
    }

    private fun Complaint.toDoc(): Map<String, Any> {
        return listOfNotNull(
            "accountId" to this.accountId.toString(),
            "state" to this.state,
            this.problemCountry?.let { "problemCountry" to it },
            this.problemDate?.let { "problemDate" to it.toString() },
            this.type?.let { "type" to it },
            "createdAt" to this.createdAt.toString(),
            "updatedAt" to this.updatedAt.toString(),
        ).toMap()
    }

    private fun fromDoc(id: UUID, doc: Map<String, Any>): Complaint {
        return Complaint(
            id,
            UUID.fromString(doc.getValue("accountId").toString()),
            Complaint.State.valueOf(doc.getValue("state").toString()),
            doc["problemCountry"]?.let { CountryCode.valueOf(it.toString()) },
            doc["problemDate"]?.let { LocalDate.parse(it.toString()) },
            doc["type"]?.let { Complaint.Type.valueOf(it.toString()) },
            Instant.parse(doc.getValue("createdAt").toString()),
            Instant.parse(doc.getValue("updatedAt").toString()),
        )
    }
}
