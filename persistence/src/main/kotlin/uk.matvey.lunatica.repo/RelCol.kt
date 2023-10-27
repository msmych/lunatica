package uk.matvey.lunatica.repo

import kotlinx.serialization.json.JsonElement
import java.time.Instant
import java.time.LocalDate
import java.util.UUID

sealed interface RelCol {

    class Uuid(val value: UUID?) : RelCol

    class Text(val value: String?) : RelCol

    class Date(val value: LocalDate?) : RelCol

    class TimeStamp(val value: Instant?) : RelCol

    class Jsonb(val value: JsonElement) : RelCol
}
