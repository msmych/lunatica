package uk.matvey.lunatica.account

import java.time.Instant
import java.util.UUID
import java.util.UUID.randomUUID

data class Account(
    val id: UUID,
    val email: String,
    val passHash: String,
    val tgChatId: Long?,
    val createdAt: Instant,
    val updatedAt: Instant
) {
    companion object {
        fun account(email: String, passHash: String): Account {
            val now = Instant.now()
            return Account(
                randomUUID(),
                email,
                passHash,
                null,
                now,
                now
            )
        }
    }
}
