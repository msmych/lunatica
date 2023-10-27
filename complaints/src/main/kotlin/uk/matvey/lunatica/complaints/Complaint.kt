package uk.matvey.lunatica.complaints

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
    val state: State,
    val problemCountry: CountryCode?,
    val problemDate: @Contextual LocalDate?,
    val type: Type?,
    val contactDetails: Map<String, String>,
    val createdAt: @Contextual Instant,
    val updatedAt: @Contextual Instant,
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
    }

    override fun id(): Uuid {
        return uuidRel(this.id)
    }

    override fun updatedAt(): TimeStamp {
        return timeStampRel(this.updatedAt)
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
