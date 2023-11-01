package uk.matvey.lunatica.yabeda

import com.neovisionaries.i18n.CountryCode
import uk.matvey.lunatica.complaint.Complaint

sealed class YabedaAction {

    class FileComplaint(val userId: Long) : YabedaAction()
    class SetProblemCountry(
        val userId: Long,
        val messageId: Int,
        val callbackQueryId: String,
        val complaint: Complaint,
        val country: CountryCode
    ) : YabedaAction()
    class SendComplaintMessage(val userId: Long, val complaint: Complaint, val text: String) : YabedaAction()
    class SetContactEmail(val userId: Long, val complaint: Complaint, val email: String) : YabedaAction()

    data object Noop : YabedaAction()
}
