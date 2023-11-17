package uk.matvey.lunatica.complaint

import com.neovisionaries.i18n.CountryCode
import java.time.Instant
import java.time.LocalDate
import java.util.UUID
import java.util.UUID.randomUUID

data class Complaint(
    val id: UUID,
    val accountId: UUID,
    val state: State,
    val problemCountry: CountryCode?,
    val problemDate: LocalDate?,
    val type: Type?,
    val createdAt: Instant,
    val updatedAt: Instant
) {
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

    companion object {
        fun new(
            accountId: UUID,
            problemCountry: CountryCode,
            problemDate: LocalDate,
            type: Type
        ): Complaint {
            val now = Instant.now()
            return Complaint(randomUUID(), accountId, State.NEW, problemCountry, problemDate, type, now, now)
        }

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
    }
}
