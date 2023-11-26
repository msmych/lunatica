package uk.matvey.lunatica.app.yabeda

import com.neovisionaries.i18n.CountryCode
import uk.matvey.lunatica.account.Account
import uk.matvey.lunatica.complaint.Complaint

sealed class YabedaAction {

    class FileComplaint(val userId: Long, val username: String) : YabedaAction()
    class SetComplaintCountry(
        val userId: Long,
        val messageId: Int,
        val callbackQueryId: String,
        val complaint: Complaint,
        val country: CountryCode
    ) : YabedaAction()

    class SetComplaintType(
        val userId: Long,
        val messageId: Int,
        val callbackQueryId: String,
        val complaint: Complaint,
        val type: Complaint.Type,
    ) : YabedaAction()

    class SendComplaintMessage(val userId: Long, val account: Account, val complaint: Complaint, val text: String) : YabedaAction()
    class SetAccountEmail(val userId: Long, val account: Account, val complaint: Complaint, val email: String) :
        YabedaAction()

    data object Noop : YabedaAction()
}
