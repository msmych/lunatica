package uk.matvey.lunatica.complaint

import com.neovisionaries.i18n.CountryCode
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import uk.matvey.lunatica.pg.Entity
import uk.matvey.lunatica.pg.RelCol.TimeStamp
import uk.matvey.lunatica.pg.RelCol.TimeStamp.Companion.timeStampRel
import uk.matvey.lunatica.pg.RelCol.Uuid
import uk.matvey.lunatica.pg.RelCol.Uuid.Companion.uuidRel
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
    val assignedTo: @Contextual UUID?,
    val createdAt: @Contextual Instant,
    val updatedAt: @Contextual Instant
) : Entity<Uuid> {
    enum class State {
        DRAFT,
        NEW,
        READ,
        IN_PROGRESS,
        RESOLVED,
        CANCELLED,
        DELETED,
    }

    enum class Type {
        BANK_ACCOUNT_OPENING_REJECTED,
        FLIGHT_ENTRY_REJECTED,
        BANK_DEPOSIT_REJECTED,
        INSTAGRAM_AD_REJECTED,
        FLAT_RENT_REJECTED,
        EMPLOYMENT_REJECTED,
        RESIDENCE_PERMIT_REJECTED,
        RESIDENCE_PERMIT_REVOKED,
        OTHER
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
            return Complaint(randomUUID(), accountId, State.NEW, problemCountry, problemDate, type, null, now, now)
        }
    }

    override fun idRel(): Uuid {
        return uuidRel(id)
    }

    override fun updatedAtRel(): TimeStamp {
        return timeStampRel(updatedAt)
    }
}
