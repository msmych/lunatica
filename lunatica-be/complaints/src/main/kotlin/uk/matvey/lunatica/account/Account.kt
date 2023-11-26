package uk.matvey.lunatica.account

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import uk.matvey.lunatica.pg.Entity
import uk.matvey.lunatica.pg.RelCol
import uk.matvey.lunatica.pg.RelCol.TimeStamp.Companion.timeStampRel
import uk.matvey.lunatica.pg.RelCol.Uuid
import uk.matvey.lunatica.pg.RelCol.Uuid.Companion.uuidRel
import java.time.Instant
import java.util.UUID
import java.util.UUID.randomUUID

@Serializable
data class Account(
    val id: @Contextual UUID,
    val email: String?,
    val passHash: String?,
    val name: String?,
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
        fun tgAccount(tgChatId: Long, name: String): Account {
            val now = Instant.now()
            return Account(
                randomUUID(),
                null,
                null,
                name,
                listOf(Role.CLIENT),
                tgChatId,
                now,
                now
            )
        }
        fun account(email: String, passHash: String, name: String?): Account {
            val now = Instant.now()
            return Account(
                randomUUID(),
                email,
                passHash,
                name,
                listOf(Role.CLIENT),
                null,
                now,
                now
            )
        }
    }

    override fun idRel(): Uuid {
        return uuidRel(id)
    }

    override fun updatedAtRel(): RelCol.TimeStamp {
        return timeStampRel(updatedAt)
    }
}
