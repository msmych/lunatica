package uk.matvey.lunatica.complaints

import com.neovisionaries.i18n.CountryCode
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.encodeToJsonElement
import org.postgresql.util.PGobject
import uk.matvey.lunatica.pg.ColumnValue
import uk.matvey.lunatica.pg.PgEntityRepo
import uk.matvey.lunatica.pg.TableRecord
import java.sql.ResultSet
import java.util.UUID
import javax.sql.DataSource

class ComplaintPgRepo(ds: DataSource, dispatcher: CoroutineDispatcher) : PgEntityRepo<ColumnValue.Uuid, Complaint>("complaints", ds, dispatcher) {

    suspend fun get(id: UUID): Complaint {
        return selectStar("where id = ?", ColumnValue.Uuid(id)).single()
    }

    suspend fun list(limit: Int): List<Complaint> {
        return selectStar("limit $limit")
    }

    suspend fun findLastDraftByTgUserId(tgUserId: Long): Complaint? {
        return selectStar("where state = 'DRAFT' and contact_details ->> 'tgUserId' = ?", ColumnValue.Text(tgUserId.toString()))
            .maxByOrNull { it.updatedAt }
    }

    override fun Complaint.toTableRecord(): TableRecord {
        return TableRecord(
            linkedMapOf(
                "id" to ColumnValue.Uuid(id),
                "state" to ColumnValue.Text(state.name),
                "problem_country" to ColumnValue.Text(problemCountry?.name),
                "problem_date" to ColumnValue.Date(problemDate),
                "type" to ColumnValue.Text(type?.name),
                "contact_details" to ColumnValue.Jsonb(Json.encodeToJsonElement(contactDetails)),
                "created_at" to ColumnValue.TimeStamp(createdAt),
                "updated_at" to ColumnValue.TimeStamp(updatedAt),
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
