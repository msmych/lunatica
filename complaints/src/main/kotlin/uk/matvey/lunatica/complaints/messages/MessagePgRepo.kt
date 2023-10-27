package uk.matvey.lunatica.complaints.messages

import kotlinx.coroutines.CoroutineDispatcher
import uk.matvey.lunatica.pg.PgEntityRepo
import uk.matvey.lunatica.repo.RelCol.Text
import uk.matvey.lunatica.repo.RelCol.TimeStamp
import uk.matvey.lunatica.repo.RelCol.Uuid
import uk.matvey.lunatica.repo.RelTab
import java.sql.ResultSet
import java.util.UUID
import javax.sql.DataSource

class MessagePgRepo(ds: DataSource, dispatcher: CoroutineDispatcher) :
    PgEntityRepo<Uuid, Message>("messages", ds, dispatcher),
    MessageRepo {

    override suspend fun listByComplaintId(complaintId: UUID): List<Message> {
        return selectStar("where complaint_id = ? order by created_at desc", Uuid(complaintId))
    }

    override fun Message.toTableRecord(): RelTab {
        return RelTab(
            linkedMapOf(
                "id" to Uuid(this.id),
                "complaint_id" to Uuid(this.complaintId),
                "content" to Text(this.content),
                "created_at" to TimeStamp(this.createdAt),
                "updated_at" to TimeStamp(this.updatedAt)
            )
        )
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
