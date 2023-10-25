package uk.matvey.lunatica.complaints

import com.neovisionaries.i18n.CountryCode
import uk.matvey.lunatica.pg.ColumnValue
import uk.matvey.lunatica.pg.Entity
import java.time.Instant
import java.time.LocalDate
import java.util.UUID
import java.util.UUID.randomUUID

data class Complaint(
    val id: UUID,
    val state: State,
    val problemCountry: CountryCode?,
    val problemDate: LocalDate?,
    val type: Type?,
    val contactDetails: Map<String, String>,
    val createdAt: Instant,
    val updatedAt: Instant,
) : Entity<ColumnValue.Uuid> {
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

    override fun id(): ColumnValue.Uuid {
        return ColumnValue.Uuid(this.id)
    }

    override fun updatedAt(): ColumnValue.TimeStamp {
        return ColumnValue.TimeStamp(this.updatedAt)
    }

    companion object {
        fun draft(
            contactDetails: Map<String, String>,
        ): Complaint {
            val now = Instant.now()
            return Complaint(
                randomUUID(),
                State.DRAFT,
                null,
                null,
                null,
                contactDetails,
                now,
                now
            )
        }

        fun new(
            problemCountry: CountryCode?,
            problemDate: LocalDate?,
            type: Type?,
            contactDetails: Map<String, String>,
        ): Complaint {
            val now = Instant.now()
            return Complaint(
                randomUUID(),
                State.NEW,
                problemCountry,
                problemDate,
                type,
                contactDetails,
                now,
                now
            )
        }
    }
}
