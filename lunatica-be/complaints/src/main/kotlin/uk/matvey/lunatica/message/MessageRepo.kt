package uk.matvey.lunatica.message

import uk.matvey.lunatica.repo.EntityRepo
import java.util.UUID

interface MessageRepo: EntityRepo<Message> {
    suspend fun listByComplaintId(complaintId: UUID): List<Message>
}
