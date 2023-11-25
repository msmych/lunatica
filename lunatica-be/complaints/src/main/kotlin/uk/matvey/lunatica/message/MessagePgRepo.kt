package uk.matvey.lunatica.message

import kotlinx.coroutines.CoroutineDispatcher
import uk.matvey.lunatica.pg.PgEntityRepo
import uk.matvey.lunatica.repo.RelCol.Text.Companion.textRel
import uk.matvey.lunatica.repo.RelCol.TimeStamp.Companion.timeStampRel
import uk.matvey.lunatica.repo.RelCol.Uuid
import uk.matvey.lunatica.repo.RelCol.Uuid.Companion.uuidRel
import uk.matvey.lunatica.repo.RelTab
import java.sql.ResultSet
import java.util.UUID
import javax.sql.DataSource

class MessagePgRepo(ds: DataSource, dispatcher: CoroutineDispatcher) :
    PgEntityRepo<Uuid, Message>("messages", ds, dispatcher),
    MessageRepo {

    override suspend fun listByComplaintId(complaintId: UUID): List<Message> {
        return selectStar("where complaint_id = ? order by created_at desc", uuidRel(complaintId))
    }

    override fun Message.toTableRecord(): RelTab {
        return RelTab(
            linkedMapOf(
                "id" to uuidRel(this.id),
                "author_id" to uuidRel(this.authorId),
                "complaint_id" to uuidRel(this.complaintId),
                "content" to textRel(this.content),
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
            this.getTimestamp("created_at").toInstant(),
            this.getTimestamp("updated_at").toInstant(),
        )
    }
}
