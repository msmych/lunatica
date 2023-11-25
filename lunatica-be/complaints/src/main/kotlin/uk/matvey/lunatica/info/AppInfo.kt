package uk.matvey.lunatica.info

import com.neovisionaries.i18n.CountryCode
import kotlinx.serialization.Serializable
import uk.matvey.lunatica.complaint.Complaint

@Serializable
data class AppInfo(
    val countries: List<CountryInfo>,
    val complaintTypes: List<ComplaintTypeInfo>,
    val complaintStates: List<ComplaintStateInfo>,
) {

    @Serializable
    data class CountryInfo(
        val code: CountryCode,
        val emoji: String,
        val nameRu: String,
    )

    @Serializable
    data class ComplaintTypeInfo(
        val code: Complaint.Type,
        val emoji: String,
        val nameRu: String,
    )

    @Serializable
    data class ComplaintStateInfo(
        val code: Complaint.State,
        val emoji: String,
        val nameRu: String,
    )
}
