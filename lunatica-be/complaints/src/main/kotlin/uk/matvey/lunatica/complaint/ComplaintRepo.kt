package uk.matvey.lunatica.complaint

import com.neovisionaries.i18n.CountryCode
import kotlinx.coroutines.CoroutineDispatcher
import uk.matvey.lunatica.pg.PgEntityRepo
import uk.matvey.lunatica.pg.RelCol.Date.Companion.dateRel
import uk.matvey.lunatica.pg.RelCol.Text.Companion.textRel
import uk.matvey.lunatica.pg.RelCol.TimeStamp.Companion.timeStampRel
import uk.matvey.lunatica.pg.RelCol.Uuid
import uk.matvey.lunatica.pg.RelCol.Uuid.Companion.uuidRel
import uk.matvey.lunatica.pg.RelTab
import java.sql.ResultSet
import java.util.UUID
import javax.sql.DataSource

class ComplaintRepo(ds: DataSource, dispatcher: CoroutineDispatcher) :
    PgEntityRepo<Uuid, Complaint>("complaints", ds, dispatcher) {

    suspend fun get(id: UUID): Complaint {
        return selectStar("where id = ?", uuidRel(id)).single()
    }

    suspend fun list(limit: Int): List<Complaint> {
        return selectStar("limit $limit")
    }

    suspend fun findLastDraftByAccountId(accountId: UUID): Complaint? {
        return selectStar(
            "where state = 'DRAFT' and account_id = ? order by updated_at desc limit 1",
            uuidRel(accountId),
        ).singleOrNull()
    }

    override fun Complaint.toTableRecord(): RelTab {
        return RelTab(
            linkedMapOf(
                "id" to uuidRel(id),
                "account_id" to uuidRel(accountId),
                "state" to textRel(state.name),
                "problem_country" to textRel(problemCountry?.name),
                "problem_date" to dateRel(problemDate),
                "type" to textRel(type?.name),
                "assigned_to" to uuidRel(assignedTo),
                "created_at" to timeStampRel(createdAt),
                "updated_at" to timeStampRel(updatedAt),
            )
        )
    }

    override fun ResultSet.toEntity(): Complaint {
        return Complaint(
            UUID.fromString(this.getObject("id").toString()),
            UUID.fromString(this.getObject("account_id").toString()),
            Complaint.State.valueOf(this.getString("state")),
            this.getString("problem_country")?.let(CountryCode::valueOf),
            this.getDate("problem_date")?.toLocalDate(),
            this.getString("type")?.let(Complaint.Type::valueOf),
            this.getObject("assigned_to")?.let { UUID.fromString(it.toString()) },
            this.getTimestamp("created_at").toInstant(),
            this.getTimestamp("updated_at").toInstant(),
        )
    }
}
