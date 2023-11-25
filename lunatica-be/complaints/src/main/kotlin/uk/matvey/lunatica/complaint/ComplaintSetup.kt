package uk.matvey.lunatica.complaint

import com.neovisionaries.i18n.CountryCode
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.contextual
import uk.matvey.lunatica.complaint.Complaint.Type.BANK_ACCOUNT_OPENING_REJECTED
import uk.matvey.lunatica.complaint.Complaint.Type.BANK_DEPOSIT_REJECTED
import uk.matvey.lunatica.complaint.Complaint.Type.EMPLOYMENT_REJECTED
import uk.matvey.lunatica.complaint.Complaint.Type.FLAT_RENT_REJECTED
import uk.matvey.lunatica.complaint.Complaint.Type.FLIGHT_ENTRY_REJECTED
import uk.matvey.lunatica.complaint.Complaint.Type.INSTAGRAM_AD_REJECTED
import uk.matvey.lunatica.complaint.Complaint.Type.OTHER
import uk.matvey.lunatica.complaint.Complaint.Type.RESIDENCE_PERMIT_REJECTED
import uk.matvey.lunatica.complaint.Complaint.Type.RESIDENCE_PERMIT_REVOKED
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
        CountryCode.IT to TgLabel("🇮🇹", "Италия"), // 21
        CountryCode.TR to TgLabel("🇹🇷", "Турция"), // 9
        CountryCode.BG to TgLabel("🇧🇬", "Болгария"), // 7
        CountryCode.HU to TgLabel("🇭🇺", "Венгрия"), // 7
        CountryCode.DE to TgLabel("🇩🇪", "Германия"), // 6
        CountryCode.ES to TgLabel("🇪🇸", "Испания"), // 6
        CountryCode.PT to TgLabel("🇵🇹", "Португалия"), // 5
        CountryCode.RO to TgLabel("🇷🇴", "Румыния"), // 5
        CountryCode.ME to TgLabel("🇲🇪", "Черногория"), // 4
        CountryCode.FR to TgLabel("🇫🇷", "Франция"), // 4
        CountryCode.MD to TgLabel("🇲🇩", "Молдова"), // 4
        CountryCode.SK to TgLabel("🇸🇰", "Словакия"), // 3
        CountryCode.RS to TgLabel("🇷🇸", "Сербия"), // 3
        CountryCode.CZ to TgLabel("🇨🇿", "Чехия"), // 3
        CountryCode.AU to TgLabel("🇦🇺", "Австралия"), // 3
        CountryCode.GE to TgLabel("🇬🇪", "Грузия"), // 2
        CountryCode.CH to TgLabel("🇨🇭", "Швейцария"), // 2
        CountryCode.HR to TgLabel("🇭🇷", "Хорватия"), // 2
        CountryCode.LV to TgLabel("🇱🇻", "Латвия"), // 2
        CountryCode.CY to TgLabel("🇨🇾", "Кипр"), // 2
        CountryCode.AT to TgLabel("🇦🇹", "Австрия"), // 1
        CountryCode.AM to TgLabel("🇦🇲", "Армения"), // 1
        CountryCode.IL to TgLabel("🇮🇱", "Израиль"), // 1
        CountryCode.US to TgLabel("🇺🇸", "США"), // 1
        CountryCode.BE to TgLabel("🇧🇪", "Бельгия"), // 1
        CountryCode.AE to TgLabel("🇦🇪", "Дубай"), // 1
        CountryCode.FI to TgLabel("🇫🇮", "Финляндия"), // 1
        CountryCode.SI to TgLabel("🇸🇮", "Словения"), // 1
        CountryCode.UNDEFINED to TgLabel("❔", "Другая")
    )

    val COMPLAINTS_TYPES = mapOf(
        BANK_ACCOUNT_OPENING_REJECTED to TgLabel("🏦", "Не дают открыть счет"),
        FLIGHT_ENTRY_REJECTED to TgLabel("🛫", "Не пустили на самолёт"),
        BANK_DEPOSIT_REJECTED to TgLabel("💰", "Не дают внести деньги на имеющийся счёт"),
        INSTAGRAM_AD_REJECTED to TgLabel("📱", "Инстаграм не даёт оплатить рекламу"),
        FLAT_RENT_REJECTED to TgLabel("🏠", "Не сдают квартиру"),
        EMPLOYMENT_REJECTED to TgLabel("👷‍♂️", "Не берут на работу"),
        RESIDENCE_PERMIT_REJECTED to TgLabel("🪪", "Отказали в ВНЖ без объяснения причин"),
        RESIDENCE_PERMIT_REVOKED to TgLabel("🧌", "Забрали ВНЖ"),
        OTHER to TgLabel("❔", "Другое"),
    )

    val COMPLAINTS_STATES = mapOf(
        Complaint.State.DRAFT to TgLabel("\uD83D\uDCDD", "Черновик"),
        Complaint.State.NEW to TgLabel("\uD83C\uDD95", "Новое"),
        Complaint.State.READ to TgLabel("\uD83D\uDC40", "Прочитанное"),
        Complaint.State.IN_PROGRESS to TgLabel("🚀", "В обработке"),
        Complaint.State.RESOLVED to TgLabel("✅", "Готово"),
        Complaint.State.CANCELLED to TgLabel("\uD83D\uDDD1\uFE0F", "Отменено"),
        Complaint.State.DELETED to TgLabel("\uD83D\uDEAB", "Удалено"),
    )

    @Serializable
    data class TgLabel(
        val emoji: String,
        val nameRu: String
    ) {
        fun label() = "$emoji $nameRu"
    }
}
