package uk.matvey.lunatica.app.yabeda

import com.neovisionaries.i18n.CountryCode
import com.pengrad.telegrambot.model.Update
import uk.matvey.lunatica.app.yabeda.YabedaAction.FileComplaint
import uk.matvey.lunatica.app.yabeda.YabedaAction.SendComplaintMessage
import uk.matvey.lunatica.app.yabeda.YabedaAction.SetAccountEmail
import uk.matvey.lunatica.app.yabeda.YabedaAction.SetComplaintCountry
import uk.matvey.lunatica.complaint.Complaint
import uk.matvey.lunatica.complaint.ComplaintRepo
import uk.matvey.lunatica.complaint.ComplaintSetup.PROBLEM_COUNTRIES
import uk.matvey.lunatica.account.AccountRepo
import uk.matvey.lunatica.message.MessageRepo

class YabedaActionSelector(
    private val accountRepo: AccountRepo,
    private val complaintRepo: ComplaintRepo,
    private val messageRepo: MessageRepo
) {

    suspend fun select(update: Update): YabedaAction {
        if (update.message()?.text() == "/complaint") {
            return FileComplaint(update.message().from().id())
        }
        var account = update.callbackQuery()?.from()?.id()?.let { accountRepo.findByTgChatId(it) }
        if (account != null) {
            complaintRepo.findLastDraftByAccountId(account.id)?.let { draftComplaint ->
                if (PROBLEM_COUNTRIES.keys.map { it.name }.contains(update.callbackQuery().data())) {
                    return SetComplaintCountry(
                        update.callbackQuery().from().id(),
                        update.callbackQuery().message().messageId(),
                        update.callbackQuery().id(),
                        draftComplaint,
                        CountryCode.valueOf(update.callbackQuery().data())
                    )
                }
                if (Complaint.Type.entries.any { it.name == update.callbackQuery().data() }) {
                    return YabedaAction.SetComplaintType(
                        update.callbackQuery().from().id(),
                        update.callbackQuery().message().messageId(),
                        update.callbackQuery().id(),
                        draftComplaint,
                        Complaint.Type.valueOf(update.callbackQuery().data())
                    )
                }
            }
        }
        account = update.message()?.from()?.id()?.let { accountRepo.findByTgChatId(it) }
        if (account != null) {
            complaintRepo.findLastDraftByAccountId(account.id)?.let { draftComplaint ->
                if (update.message().text() != null) {
                    return if (messageRepo.listByComplaintId(draftComplaint.id).isEmpty())
                        SendComplaintMessage(
                            update.message().from().id(),
                            draftComplaint,
                            update.message().text()
                        )
                    else SetAccountEmail(
                        update.message().from().id(),
                        account,
                        draftComplaint,
                        update.message().text()
                    )
                }
            }
        }
        return YabedaAction.Noop
    }
}
