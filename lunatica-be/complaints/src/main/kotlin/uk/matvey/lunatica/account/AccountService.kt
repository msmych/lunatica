package uk.matvey.lunatica.account

import uk.matvey.lunatica.sha256
import java.util.UUID

class AccountService(private val accountRepo: AccountRepo) {

    suspend fun createAccount(email: String, pass: String, name: String?): UUID {
        val account = Account.account(email, sha256(pass), name)
        accountRepo.insert(account)
        return account.id
    }

    suspend fun updateAccount(id: UUID, name: String?, pass: String?) {
        val account = accountRepo.get(id)
        accountRepo.update(
            account.copy(
                name = name ?: account.name,
                passHash = pass?.let(::sha256) ?: account.passHash
            )
        )
    }

    suspend fun ensureTgAccount(tgUserId: Long, name: String): Account {
        return accountRepo.findByTgChatId(tgUserId) ?: Account.tgAccount(tgUserId, name).also { accountRepo.insert(it) }
    }

    suspend fun updateAccountEmail(account: Account, email: String) {
        accountRepo.update(account.copy(email = email))
    }

    suspend fun toggleAccountRole(id: UUID, role: Account.Role) {
        val account = accountRepo.get(id)
        val roles = if (role in account.roles) account.roles - role else account.roles + role
        accountRepo.update(account.copy(roles = roles))
    }
}
