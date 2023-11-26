package uk.matvey.lunatica.message

import kotlinx.coroutines.CoroutineDispatcher
import uk.matvey.lunatica.pg.PgEntityRepo
import uk.matvey.lunatica.pg.RelCol.Text.Companion.textRel
import uk.matvey.lunatica.pg.RelCol.TimeStamp.Companion.timeStampRel
import uk.matvey.lunatica.pg.RelCol.Uuid
import uk.matvey.lunatica.pg.RelCol.Uuid.Companion.uuidRel
import uk.matvey.lunatica.pg.RelTab
import java.sql.ResultSet
import java.util.UUID
import javax.sql.DataSource

class MessageRepo(ds: DataSource, dispatcher: CoroutineDispatcher) : PgEntityRepo<Uuid, Message>("messages", ds, dispatcher) {

    suspend fun get(id: UUID): Message {
        return selectStar("where id = ?", uuidRel(id)).single()
    }

    suspend fun listByComplaintId(complaintId: UUID): List<Message> {
        return selectStar("where complaint_id = ? order by created_at desc", uuidRel(complaintId))
    }

    override fun Message.toTableRecord(): RelTab {
        return RelTab(
            linkedMapOf(
                "id" to uuidRel(this.id),
                "author_id" to uuidRel(this.authorId),
                "complaint_id" to uuidRel(this.complaintId),
                "content" to textRel(this.content),
                "attachment_key" to textRel(this.attachmentKey),
                "created_at" to timeStampRel(this.createdAt),
                "updated_at" to timeStampRel(this.updatedAt)
            )
        )
    }

    override fun ResultSet.toEntity(): Message {
        return Message(
            UUID.fromString(this.getObject("id").toString()),
            UUID.fromString(this.getObject("author_id").toString()),
            UUID.fromString(this.getObject("complaint_id").toString()),
            this.getString("content"),
            this.getString("attachment_key"),
            this.getTimestamp("created_at").toInstant(),
            this.getTimestamp("updated_at").toInstant(),
        )
    }
}
