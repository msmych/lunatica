package uk.matvey.lunatica.complaint

import com.neovisionaries.i18n.CountryCode
import java.time.Instant
import java.time.LocalDate
import java.util.UUID

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
    }

    companion object {
        fun new(
            accountId: UUID,
            problemCountry: CountryCode,
            problemDate: LocalDate,
            type: Type
        ): Complaint {
            val now = Instant.now()
            return Complaint(UUID.randomUUID(), accountId, State.NEW, problemCountry, problemDate, type, now, now)
        }
    }
}
