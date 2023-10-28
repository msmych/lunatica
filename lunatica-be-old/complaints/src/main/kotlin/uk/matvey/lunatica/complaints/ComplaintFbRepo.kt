package uk.matvey.lunatica.complaints

import com.google.cloud.firestore.FieldPath
import com.google.cloud.firestore.Firestore
import com.neovisionaries.i18n.CountryCode
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

    override fun Map<String, Any?>.toEntity(id: String): Complaint {
        return Complaint(
            UUID.fromString(id),
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
        return withCollection { coll ->
            requireNotNull(coll.document(id.toString()).get().get().data)
                .toEntity(id.toString())
        }
    }

    override suspend fun list(limit: Int): List<Complaint> {
        return withCollection { coll ->
            coll.limit(limit)
                .get().get()
                .map { it.data.toEntity(it.id) }
        }
    }

    override suspend fun findAllDraftByTgUserId(tgUserId: Long): List<Complaint> {
        return withCollection { coll ->
            coll.whereEqualTo(FieldPath.of("contactDetails", "tgUserId"), tgUserId.toString())
                .whereEqualTo("state", Complaint.State.DRAFT)
                .get().get()
                .map { it.data.toEntity(it.id) }
        }
    }

    override suspend fun findLastDraftByTgUserId(tgUserId: Long): Complaint? {
        return findAllDraftByTgUserId(tgUserId).maxByOrNull { it.updatedAt }
    }
}
