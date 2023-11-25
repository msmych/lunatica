package uk.matvey.lunatica.account

import uk.matvey.lunatica.sha256
import java.util.UUID

class AccountService(private val accountRepo: AccountPgRepo) {

    suspend fun createAccount(email: String, pass: String): UUID {
        val account = Account.account(email, sha256(pass))
        accountRepo.insert(account)
        return account.id
    }

    suspend fun updateAccount(id: UUID, pass: String?) {
        val account = accountRepo.get(id)
        accountRepo.update(account.copy(passHash = pass?.let(::sha256) ?: account.passHash))
    }

    suspend fun ensureTgAccount(tgUserId: Long): Account {
        return accountRepo.findByTgChatId(tgUserId) ?: Account.tgAccount(tgUserId).also { accountRepo.insert(it) }
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
