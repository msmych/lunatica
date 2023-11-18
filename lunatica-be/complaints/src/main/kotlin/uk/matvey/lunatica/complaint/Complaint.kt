package uk.matvey.lunatica.complaint

import com.neovisionaries.i18n.CountryCode
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import uk.matvey.lunatica.repo.Entity
import uk.matvey.lunatica.repo.RelCol.TimeStamp
import uk.matvey.lunatica.repo.RelCol.TimeStamp.Companion.timeStampRel
import uk.matvey.lunatica.repo.RelCol.Uuid
import uk.matvey.lunatica.repo.RelCol.Uuid.Companion.uuidRel
import java.time.Instant
import java.time.LocalDate
import java.util.UUID
import java.util.UUID.randomUUID

@Serializable
data class Complaint(
    val id: @Contextual UUID,
    val accountId: @Contextual UUID,
    val state: State,
    val problemCountry: CountryCode?,
    val problemDate: @Contextual LocalDate?,
    val type: Type?,
    val createdAt: @Contextual Instant,
    val updatedAt: @Contextual Instant
) : Entity<Uuid> {
    enum class State {
        DRAFT,
        NEW,
        READ,
        RESOLVED,
        CANCELLED,
        DELETED,
    }

    enum class Type {
        BANK,
        AIRLINE,
        OTHER,
    }

    fun update(state: State?): Complaint {
        return copy(
            state = state ?: this.state,
        )
    }

    companion object {
        fun draft(
            accountId: UUID,
        ): Complaint {
            val now = Instant.now()
            return Complaint(
                randomUUID(),
                accountId,
                State.DRAFT,
                null,
                null,
                null,
                now,
                now
            )
        }

        fun new(
            accountId: UUID,
            problemCountry: CountryCode,
            problemDate: LocalDate,
            type: Type
        ): Complaint {
            val now = Instant.now()
            return Complaint(randomUUID(), accountId, State.NEW, problemCountry, problemDate, type, now, now)
        }
    }

    override fun id(): Uuid {
        return uuidRel(id)
    }

    override fun updatedAt(): TimeStamp {
        return timeStampRel(updatedAt)
    }
}
