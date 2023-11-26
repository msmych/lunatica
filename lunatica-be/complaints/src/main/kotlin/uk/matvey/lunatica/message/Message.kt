package uk.matvey.lunatica.message

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import uk.matvey.lunatica.pg.Entity
import uk.matvey.lunatica.pg.RelCol.TimeStamp
import uk.matvey.lunatica.pg.RelCol.TimeStamp.Companion.timeStampRel
import uk.matvey.lunatica.pg.RelCol.Uuid
import uk.matvey.lunatica.pg.RelCol.Uuid.Companion.uuidRel
import java.time.Instant
import java.util.UUID
import java.util.UUID.randomUUID

@Serializable
data class Message(
    val id: @Contextual UUID,
    val authorId: @Contextual UUID,
    val complaintId: @Contextual UUID?,
    val content: String,
    val attachmentKey: String?,
    val createdAt: @Contextual Instant,
    val updatedAt: @Contextual Instant,
) : Entity<Uuid> {
    override fun idRel(): Uuid {
        return uuidRel(id)
    }

    override fun updatedAtRel(): TimeStamp {
        return timeStampRel(updatedAt)
    }

    companion object {

        fun complaintMessage(authorId: UUID, complaintId: UUID, content: String): Message {
            val now = Instant.now()
            return Message(randomUUID(), authorId, complaintId, content, null, now, now)
        }

        fun complaintAttachment(authorId: UUID, complaintId: UUID, fileName: String): Message {
            val now = Instant.now()
            val id = randomUUID()
            return Message(id, authorId, complaintId, fileName, id.toString(), now, now)
        }
    }
}
