package uk.matvey.lunatica.complaints.account

import uk.matvey.lunatica.repo.EntityRepo

interface AccountRepo : EntityRepo<Account> {

    suspend fun findByEmail(email: String): Account?
    suspend fun findByTgChatId(tgChatId: Long): Account?
}
