package uk.matvey.lunatica.app.yabeda

import com.pengrad.telegrambot.TelegramBot
import com.pengrad.telegrambot.model.request.ParseMode
import com.pengrad.telegrambot.request.SendMessage
import uk.matvey.lunatica.account.AccountService
import uk.matvey.lunatica.complaint.Complaint
import uk.matvey.lunatica.complaint.ComplaintService

suspend fun setAccountEmail(
    accountService: AccountService,
    complaintService: ComplaintService,
    action: YabedaAction.SetAccountEmail,
    bot: TelegramBot
) {
    accountService.updateAccountEmail(action.account, action.email)
    complaintService.updateComplaintState(action.complaint, Complaint.State.NEW)
    bot.execute(
        SendMessage(
            action.userId,
            "Номер вашей жалобы: `${action.complaint.id}`"
        ).parseMode(ParseMode.MarkdownV2)
    )
}
