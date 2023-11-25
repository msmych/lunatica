package uk.matvey.lunatica.app.yabeda

import com.pengrad.telegrambot.TelegramBot
import uk.matvey.lunatica.account.AccountService
import uk.matvey.lunatica.app.AppConfig
import uk.matvey.lunatica.app.yabeda.YabedaAction.SetAccountEmail
import uk.matvey.lunatica.complaint.Complaint
import uk.matvey.lunatica.complaint.ComplaintService

suspend fun setAccountEmail(
    accountService: AccountService,
    complaintService: ComplaintService,
    action: SetAccountEmail,
    bot: TelegramBot,
    config: AppConfig,
) {
    accountService.updateAccountEmail(action.account, action.email)
    complaintService.updateComplaintState(action.complaint, Complaint.State.NEW)
    bot.sendComplaintLink(action.userId, action.complaint.id, config)
}
