package uk.matvey.lunatica.complaints

import uk.matvey.lunatica.pg.EntityRepo
import java.util.UUID

interface ComplaintRepo : EntityRepo<Complaint> {
    suspend fun get(id: UUID): Complaint

    suspend fun list(limit: Int): List<Complaint>

    suspend fun findLastDraftByTgUserId(tgUserId: Long): Complaint?
}
