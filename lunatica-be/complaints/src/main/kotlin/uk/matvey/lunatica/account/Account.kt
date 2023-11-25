package uk.matvey.lunatica.account

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import uk.matvey.lunatica.repo.Entity
import uk.matvey.lunatica.repo.RelCol
import uk.matvey.lunatica.repo.RelCol.TimeStamp.Companion.timeStampRel
import uk.matvey.lunatica.repo.RelCol.Uuid
import uk.matvey.lunatica.repo.RelCol.Uuid.Companion.uuidRel
import java.time.Instant
import java.util.UUID
import java.util.UUID.randomUUID

@Serializable
data class Account(
    val id: @Contextual UUID,
    val email: String?,
    val passHash: String?,
    val roles: List<Role>,
    val tgChatId: Long?,
    val createdAt: @Contextual Instant,
    val updatedAt: @Contextual Instant
) : Entity<Uuid> {

    enum class Role {
        ADMIN,
        WORKER,
        CLIENT,
    }

    companion object {
        fun tgAccount(tgChatId: Long): Account {
            val now = Instant.now()
            return Account(
                randomUUID(),
                null,
                null,
                listOf(Role.CLIENT),
                tgChatId,
                now,
                now
            )
        }
        fun account(email: String, passHash: String): Account {
            val now = Instant.now()
            return Account(
                randomUUID(),
                email,
                passHash,
                listOf(Role.CLIENT),
                null,
                now,
                now
            )
        }
    }

    override fun id(): Uuid {
        return uuidRel(id)
    }

    override fun updatedAt(): RelCol.TimeStamp {
        return timeStampRel(updatedAt)
    }
}
