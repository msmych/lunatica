package uk.matvey.lunatica.message

import java.time.Instant
import java.util.UUID
import java.util.UUID.randomUUID

data class Message(
    val id: UUID,
    val complaintId: UUID?,
    val content: String,
    val createdAt: Instant,
    val updatedAt: Instant
) {
    companion object {
        fun complaintMessage(complaintId: UUID, content: String): Message {
            val now = Instant.now()
            return Message(
                randomUUID(),
                complaintId,
                content,
                now,
                now
            )
        }
    }
}
