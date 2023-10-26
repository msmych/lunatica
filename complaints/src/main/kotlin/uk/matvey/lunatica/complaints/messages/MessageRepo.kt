package uk.matvey.lunatica.complaints.messages

import uk.matvey.lunatica.pg.EntityRepo
import java.util.UUID

interface MessageRepo: EntityRepo<Message> {
    suspend fun listByComplaintId(complaintId: UUID): List<Message>
}
