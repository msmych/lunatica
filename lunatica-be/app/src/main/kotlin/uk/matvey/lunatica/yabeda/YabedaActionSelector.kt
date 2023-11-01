package uk.matvey.lunatica.yabeda

import com.neovisionaries.i18n.CountryCode
import com.pengrad.telegrambot.TelegramBot
import com.pengrad.telegrambot.model.Update
import uk.matvey.lunatica.complaint.ComplaintRepo
import uk.matvey.lunatica.message.MessageRepo
import uk.matvey.lunatica.yabeda.YabedaSetup.PROBLEM_COUNTRIES

class YabedaActionSelector(
    private val complaintRepo: ComplaintRepo,
    private val messageRepo: MessageRepo,
    private val bot: TelegramBot
) {

    fun select(update: Update): YabedaAction {
        if (update.message()?.text() == "/complaint") {
            return YabedaAction.FileComplaint(update.message().from().id())
        }
        var draftComplaint = update.callbackQuery()?.from()?.id()?.let { complaintRepo.findLastDraftByTgUserId(it) }
        if (draftComplaint != null) {
            if (PROBLEM_COUNTRIES.keys.map { it.name }.contains(update.callbackQuery().data())) {
                return YabedaAction.SetProblemCountry(
                    update.callbackQuery().from().id(),
                    update.callbackQuery().message().messageId(),
                    update.callbackQuery().id(),
                    draftComplaint,
                    CountryCode.valueOf(update.callbackQuery().data())
                )
            }
        }
        draftComplaint = update.message()?.from()?.id()?.let { complaintRepo.findLastDraftByTgUserId(it) }
        if (draftComplaint != null) {
            if (update.message().text() != null) {
                return if (messageRepo.listByComplaintId(draftComplaint.id).isEmpty())
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
