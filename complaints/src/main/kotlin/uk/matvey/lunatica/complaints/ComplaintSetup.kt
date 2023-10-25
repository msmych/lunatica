package uk.matvey.lunatica.complaints

import com.neovisionaries.i18n.CountryCode

object ComplaintSetup {

    val PROBLEM_COUNTRIES = mapOf(
        CountryCode.GB to CountryInfo("🇬🇧", "Великобритания"),
        CountryCode.IT to CountryInfo("🇮🇹", "Италия"),
        CountryCode.DE to CountryInfo("🇩🇪", "Германия"),
        CountryCode.FR to CountryInfo("🇫🇷", "Франция"),
    )

    data class CountryInfo(
        val emoji: String,
        val nameRu: String
    )
}
