package uk.matvey.lunatica.complaints

import com.google.cloud.firestore.FieldPath
import com.google.cloud.firestore.Firestore
import com.neovisionaries.i18n.CountryCode
import kotlinx.coroutines.withContext
import uk.matvey.lunatica.fb.FbRepo
import java.time.Instant
import java.time.LocalDate
import java.util.UUID
import kotlin.coroutines.CoroutineContext

class ComplaintFbRepo(db: Firestore, dispatcher: CoroutineContext) : FbRepo<Complaint>("complaints", db, dispatcher),
    ComplaintRepo {
    override fun Complaint.toDoc(): Map<String, Any?> {
        return linkedMapOf(
            "id" to this.id.toString(),
            "state" to this.state,
            "problemCountry" to this.problemCountry,
            "problemDate" to this.problemDate?.toString(),
            "type" to this.type?.toString(),
            "contactDetails" to this.contactDetails,
            "createdAt" to this.createdAt.toString(),
            "updatedAt" to this.updatedAt.toString(),
        )
            .mapNotNull { (k, v) -> v?.let { k to v } }
            .toMap()
    }

    override fun Map<String, Any?>.toEntity(): Complaint {
        return Complaint(
            UUID.fromString(this.getValue("id").toString()),
            Complaint.State.valueOf(this.getValue("state").toString()),
            this["problemCountry"]?.toString()?.let(CountryCode::valueOf),
            this["problemDate"]?.toString()?.let(LocalDate::parse),
            this["type"]?.toString()?.let(Complaint.Type::valueOf),
            this["contactDetails"] as Map<String, String>,
            Instant.parse(this.getValue("createdAt").toString()),
            Instant.parse(this.getValue("updatedAt").toString()),
        )
    }

    override suspend fun get(id: UUID): Complaint {
        return withContext(dispatcher) {
            requireNotNull(db.collection(collectionName).document(id.toString()).get().get().data).toEntity()
        }
    }

    override suspend fun list(limit: Int): List<Complaint> {
        return withContext(dispatcher) {
            db.collection(collectionName)
                .limit(limit)
                .get().get()
                .map { it.data.toEntity() }
        }
    }

    override suspend fun findLastDraftByTgUserId(tgUserId: Long): Complaint? {
        return withContext(dispatcher) {
            db.collection(collectionName)
                .whereEqualTo(FieldPath.of("contactDetails", "tgUserId"), tgUserId.toString())
                .get().get()
                .map { it.data.toEntity() }
                .maxByOrNull { it.updatedAt }
        }
    }
}
