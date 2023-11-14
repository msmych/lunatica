package uk.matvey.lunatica.yabeda

import com.neovisionaries.i18n.CountryCode
import uk.matvey.lunatica.complaint.Complaint

object YabedaSetup {
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
