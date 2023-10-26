package uk.matvey.lunatica.app.yabeda

import com.neovisionaries.i18n.CountryCode
import com.pengrad.telegrambot.TelegramBot
import com.pengrad.telegrambot.model.Update
import uk.matvey.lunatica.complaints.ComplaintRepo
import uk.matvey.lunatica.complaints.ComplaintSetup.PROBLEM_COUNTRIES
import uk.matvey.lunatica.complaints.messages.MessageRepo

class YabedaActionSelector(
    private val complaintRepo: ComplaintRepo,
    private val messagePgRepo: MessageRepo,
    private val bot: TelegramBot
) {

    suspend fun select(update: Update): YabedaAction {
        if (update.message()?.text() == "/complaint") {
            return YabedaAction.FileComplaint(update.message().from().id())
        }
        var draftComplaint = update.callbackQuery()?.from()?.id()?.let { complaintRepo.findLastDraftByTgUserId(it) }
        if (draftComplaint != null) {
            if (PROBLEM_COUNTRIES.keys.map { it.name }.contains(update.callbackQuery().data())) {
                return YabedaAction.SetProblemCountry(
                    update.callbackQuery().from().id(),
                    update.callbackQuery().message().messageId(),
                    draftComplaint,
                    CountryCode.valueOf(update.callbackQuery().data())
                )
            }
        }
        draftComplaint = update.message()?.from()?.id()?.let { complaintRepo.findLastDraftByTgUserId(it) }
        if (draftComplaint != null) {
            if (update.message().text() != null) {
                return if (messagePgRepo.listByComplaintId(draftComplaint.id).isEmpty())
                    YabedaAction.SendComplaintMessage(
                        update.message().from().id(),
                        draftComplaint,
                        update.message().text()
                    )
                else YabedaAction.SetContactEmail(update.message().from().id(), draftComplaint, update.message().text())
            }
        }
        return YabedaAction.Noop
    }
}
