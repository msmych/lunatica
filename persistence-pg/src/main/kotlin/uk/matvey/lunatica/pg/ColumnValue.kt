package uk.matvey.lunatica.pg

import kotlinx.serialization.json.JsonElement
import java.time.Instant
import java.time.LocalDate
import java.util.UUID

sealed interface ColumnValue {

    class Uuid(val value: UUID?) : ColumnValue

    class Text(val value: String?) : ColumnValue

    class Date(val value: LocalDate?) : ColumnValue

    class TimeStamp(val value: Instant?) : ColumnValue

    class Jsonb(val value: JsonElement) : ColumnValue
}
