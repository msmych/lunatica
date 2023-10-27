package uk.matvey.lunatica.complaints.messages

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import uk.matvey.lunatica.repo.Entity
import uk.matvey.lunatica.repo.RelCol
import uk.matvey.lunatica.repo.RelCol.TimeStamp
import uk.matvey.lunatica.repo.RelCol.Uuid
import java.time.Instant
import java.util.UUID
import java.util.UUID.randomUUID

@Serializable
data class Message(
    val id: @Contextual UUID,
    val complaintId: @Contextual UUID?,
    val content: String,
    val createdAt: @Contextual Instant,
    val updatedAt: @Contextual Instant,
) : Entity<Uuid> {
    override fun id(): Uuid {
        return Uuid(id)
    }

    override fun updatedAt(): TimeStamp {
        return TimeStamp(updatedAt)
    }

    companion object {

        fun complaintMessage(complaintId: UUID, content: String): Message {
            val now = Instant.now()
            return Message(randomUUID(), complaintId, content, now, now)
        }
    }
}
