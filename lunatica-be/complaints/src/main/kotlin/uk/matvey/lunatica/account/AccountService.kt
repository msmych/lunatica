package uk.matvey.lunatica.account

import java.util.UUID

class AccountService(private val accountRepo: AccountRepo) {

    suspend fun createAccount(email: String, pass: String): UUID {
        val account = Account.account(email, sha256(pass))
        accountRepo.insert(account)
        return account.id
    }
}
