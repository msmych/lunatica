package uk.matvey.lunatica.message

import java.time.Instant
import java.util.UUID

data class Message(
    val id: UUID,
    val complaintId: UUID?,
    val content: String,
    val createdAt: Instant,
    val updatedAt: Instant
)
