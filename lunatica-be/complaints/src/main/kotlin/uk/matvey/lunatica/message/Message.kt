package uk.matvey.lunatica.message

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import uk.matvey.lunatica.repo.Entity
import uk.matvey.lunatica.repo.RelCol.TimeStamp
import uk.matvey.lunatica.repo.RelCol.TimeStamp.Companion.timeStampRel
import uk.matvey.lunatica.repo.RelCol.Uuid
import uk.matvey.lunatica.repo.RelCol.Uuid.Companion.uuidRel
import java.time.Instant
import java.util.UUID
import java.util.UUID.randomUUID

@Serializable
data class Message(
    val id: @Contextual UUID,
    val authorId: @Contextual UUID,
    val complaintId: @Contextual UUID?,
    val content: String,
    val createdAt: @Contextual Instant,
    val updatedAt: @Contextual Instant,
) : Entity<Uuid> {
    override fun id(): Uuid {
        return uuidRel(id)
    }

    override fun updatedAt(): TimeStamp {
        return timeStampRel(updatedAt)
    }

    companion object {

        fun complaintMessage(authorId: UUID, complaintId: UUID, content: String): Message {
            val now = Instant.now()
            return Message(randomUUID(), authorId, complaintId, content, now, now)
        }
    }
}
