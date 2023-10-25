package uk.matvey.lunatica.complaints

import kotlinx.coroutines.CoroutineDispatcher
import uk.matvey.lunatica.pg.ColumnValue
import uk.matvey.lunatica.pg.PgEntityRepo
import uk.matvey.lunatica.pg.TableRecord
import java.sql.ResultSet
import java.util.UUID
import javax.sql.DataSource

class MessagePgRepo(ds: DataSource, dispatcher: CoroutineDispatcher) : PgEntityRepo<ColumnValue.Uuid, Message>("messages", ds, dispatcher) {

    suspend fun listByComplaintId(complaintId: UUID): List<Message> {
        return selectStar("where complaint_id = ? order by created_at desc", ColumnValue.Uuid(complaintId))
    }
    override fun Message.toTableRecord(): TableRecord {
        return TableRecord(linkedMapOf(
            "id" to ColumnValue.Uuid(this.id),
            "complaint_id" to ColumnValue.Uuid(this.complaintId),
            "content" to ColumnValue.Text(this.content),
            "created_at" to ColumnValue.TimeStamp(this.createdAt),
            "updated_at" to ColumnValue.TimeStamp(this.updatedAt)
        ))
    }

    override fun ResultSet.toEntity(): Message {
        return Message(
            UUID.fromString(this.getObject("id").toString()),
            UUID.fromString(this.getObject("complaint_id").toString()),
            this.getString("content"),
            this.getTimestamp("created_at").toInstant(),
            this.getTimestamp("updated_at").toInstant(),
        )
    }
}
