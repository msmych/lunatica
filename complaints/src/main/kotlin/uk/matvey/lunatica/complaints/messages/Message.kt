package uk.matvey.lunatica.complaints.messages

import uk.matvey.lunatica.pg.ColumnValue
import uk.matvey.lunatica.pg.Entity
import java.time.Instant
import java.util.UUID

data class Message(
    val id: UUID,
    val complaintId: UUID?,
    val content: String,
    val createdAt: Instant,
    val updatedAt: Instant,
) : Entity<ColumnValue.Uuid> {
    override fun id(): ColumnValue.Uuid {
        return ColumnValue.Uuid(id)
    }

    override fun updatedAt(): ColumnValue.TimeStamp {
        return ColumnValue.TimeStamp(updatedAt)
    }

    companion object {

        fun complaintMessage(complaintId: UUID, content: String): Message {
            val now = Instant.now()
            return Message(UUID.randomUUID(), complaintId, content, now, now)
        }
    }
}
