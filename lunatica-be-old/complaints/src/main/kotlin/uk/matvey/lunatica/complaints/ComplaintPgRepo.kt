package uk.matvey.lunatica.complaints

import com.neovisionaries.i18n.CountryCode
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.encodeToJsonElement
import org.postgresql.util.PGobject
import uk.matvey.lunatica.complaints.ComplaintSetup.JSON
import uk.matvey.lunatica.pg.PgEntityRepo
import uk.matvey.lunatica.repo.RelCol.Date.Companion.dateRel
import uk.matvey.lunatica.repo.RelCol.Jsonb.Companion.jsonbRel
import uk.matvey.lunatica.repo.RelCol.Text.Companion.textRel
import uk.matvey.lunatica.repo.RelCol.TimeStamp.Companion.timeStampRel
import uk.matvey.lunatica.repo.RelCol.Uuid
import uk.matvey.lunatica.repo.RelCol.Uuid.Companion.uuidRel
import uk.matvey.lunatica.repo.RelTab
import java.sql.ResultSet
import java.util.UUID
import javax.sql.DataSource

class ComplaintPgRepo(ds: DataSource, dispatcher: CoroutineDispatcher) :
    PgEntityRepo<Uuid, Complaint>("complaints", ds, dispatcher),
    ComplaintRepo {

    override suspend fun get(id: UUID): Complaint {
        return selectStar("where id = ?", uuidRel(id)).single()
    }

    override suspend fun list(limit: Int): List<Complaint> {
        return selectStar("limit $limit")
    }

    override suspend fun findAllDraftByTgUserId(tgUserId: Long): List<Complaint> {
        return selectStar(
            "where state = 'DRAFT' and contact_details ->> 'tgUserId' = ?",
            textRel(tgUserId.toString())
        )
    }

    override suspend fun findLastDraftByTgUserId(tgUserId: Long): Complaint? {
        return findAllDraftByTgUserId(tgUserId).maxByOrNull { it.updatedAt }
    }

    override fun Complaint.toTableRecord(): RelTab {
        return RelTab(
            linkedMapOf(
                "id" to uuidRel(id),
                "state" to textRel(state.name),
                "problem_country" to textRel(problemCountry?.name),
                "problem_date" to dateRel(problemDate),
                "type" to textRel(type?.name),
                "contact_details" to jsonbRel(JSON.encodeToJsonElement(contactDetails)),
                "created_at" to timeStampRel(createdAt),
                "updated_at" to timeStampRel(updatedAt),
            )
        )
    }

    override fun ResultSet.toEntity(): Complaint {
        return Complaint(
            UUID.fromString(this.getObject("id").toString()),
            Complaint.State.valueOf(this.getString("state")),
            this.getString("problem_country")?.let(CountryCode::valueOf),
            this.getDate("problem_date")?.toLocalDate(),
            this.getString("type")?.let(Complaint.Type::valueOf),
            (this.getObject("contact_details") as PGobject).value?.let { Json.decodeFromString(it) } ?: mapOf(),
            this.getTimestamp("created_at").toInstant(),
            this.getTimestamp("updated_at").toInstant(),
        )
    }
}
