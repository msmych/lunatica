package uk.matvey.lunatica.complaints

import com.neovisionaries.i18n.CountryCode
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.contextual
import java.time.Instant
import java.time.LocalDate
import java.util.UUID

object ComplaintSetup {

    val JSON = Json {
        serializersModule = SerializersModule {
            contextual(UuidSerializer)
            contextual(InstantSerializer)
            contextual(LocalDateSerializer)
        }
    }

    object UuidSerializer : KSerializer<UUID> {
        override val descriptor = PrimitiveSerialDescriptor("UUID", PrimitiveKind.STRING)

        override fun deserialize(decoder: Decoder): UUID {
            return UUID.fromString(decoder.decodeString())
        }

        override fun serialize(encoder: Encoder, value: UUID) {
            return encoder.encodeString(value.toString())
        }
    }

    object InstantSerializer : KSerializer<Instant> {
        override val descriptor = PrimitiveSerialDescriptor("Instant", PrimitiveKind.STRING)

        override fun deserialize(decoder: Decoder): Instant {
            return Instant.parse(decoder.decodeString())
        }

        override fun serialize(encoder: Encoder, value: Instant) {
            return encoder.encodeString(value.toString())
        }
    }

    object LocalDateSerializer : KSerializer<LocalDate> {
        override val descriptor = PrimitiveSerialDescriptor("LocalDate", PrimitiveKind.STRING)

        override fun deserialize(decoder: Decoder): LocalDate {
            return LocalDate.parse(decoder.decodeString())
        }

        override fun serialize(encoder: Encoder, value: LocalDate) {
            return encoder.encodeString(value.toString())
        }
    }

    val PROBLEM_COUNTRIES = mapOf(
        CountryCode.GB to TgLabel("🇬🇧", "Великобритания"),
        CountryCode.IT to TgLabel("🇮🇹", "Италия"),
        CountryCode.DE to TgLabel("🇩🇪", "Германия"),
        CountryCode.FR to TgLabel("🇫🇷", "Франция"),
        CountryCode.UNDEFINED to TgLabel("❔", "Другая"),
    )

    fun Complaint.Type.toTgLabel() = when (this) {
        Complaint.Type.BANK -> TgLabel("🏦", "Банк")
        Complaint.Type.AIRLINE -> TgLabel("🛫", "Авиакомпания")
        Complaint.Type.OTHER -> TgLabel("❔", "Другое")
    }

    data class TgLabel(
        val emoji: String,
        val nameRu: String
    ) {
        fun label() = "$emoji $nameRu"
    }
}
