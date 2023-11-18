package uk.matvey.lunatica.account

import uk.matvey.lunatica.sha256
import java.util.UUID

class AccountService(private val accountRepo: AccountRepo) {

    suspend fun createAccount(email: String, pass: String): UUID {
        val account = Account.account(email, sha256(pass))
        accountRepo.insert(account)
        return account.id
    }

    suspend fun ensureTgAccount(tgUserId: Long): Account {
        return accountRepo.findByTgChatId(tgUserId) ?: Account.tgAccount(tgUserId).also { accountRepo.insert(it) }
    }

    suspend fun updateAccountEmail(account: Account, email: String) {
        accountRepo.update(account.copy(email = email))
    }
}
