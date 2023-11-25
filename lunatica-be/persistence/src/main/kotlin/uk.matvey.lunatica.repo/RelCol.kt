package uk.matvey.lunatica.repo

import kotlinx.serialization.json.JsonElement
import java.time.Instant
import java.time.LocalDate
import java.util.UUID

sealed interface RelCol {

    class Uuid(val value: UUID?) : RelCol {
        companion object {
            fun uuidRel(value: UUID?) = Uuid(value)
        }
    }

    class Text(val value: String?) : RelCol {
        companion object {
            fun textRel(value: String?) = Text(value)
        }
    }

    class Num(val value: Long?) : RelCol {
        companion object {
            fun numRel(value: Long?) = Num(value)
        }
    }

    class Date(val value: LocalDate?) : RelCol {
        companion object {
            fun dateRel(value: LocalDate?) = Date(value)
        }
    }

    class TimeStamp(val value: Instant?) : RelCol {
        companion object {
            fun timeStampRel(value: Instant?) = TimeStamp(value)
        }
    }

    class TextArray(val value: List<String>) : RelCol {
        companion object {
            fun textArrayRel(value: List<String>) = TextArray(value)
        }
    }

    class Jsonb(val value: JsonElement) : RelCol {
        companion object {
            fun jsonbRel(value: JsonElement) = Jsonb(value)
        }
    }
}
