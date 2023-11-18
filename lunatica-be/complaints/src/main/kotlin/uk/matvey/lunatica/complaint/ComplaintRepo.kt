package uk.matvey.lunatica.complaint

import uk.matvey.lunatica.repo.EntityRepo
import java.util.UUID

interface ComplaintRepo : EntityRepo<Complaint> {
    suspend fun get(id: UUID): Complaint

    suspend fun list(limit: Int): List<Complaint>

    suspend fun findAllDraftByTgUserId(tgUserId: Long): List<Complaint>

    suspend fun findLastDraftByAccountId(accountId: UUID): Complaint?
}
