package uk.matvey.lunatica.app.yabeda

import com.pengrad.telegrambot.TelegramBot
import com.pengrad.telegrambot.model.request.ParseMode
import com.pengrad.telegrambot.request.SendMessage
import uk.matvey.lunatica.complaints.Complaint
import uk.matvey.lunatica.complaints.ComplaintRepo
import uk.matvey.lunatica.complaints.account.AccountRepo

suspend fun setAccountEmail(
    accountRepo: AccountRepo,
    complaintRepo: ComplaintRepo,
    action: YabedaAction.SetAccountEmail,
    bot: TelegramBot
) {
    accountRepo.update(action.account.copy(email = action.email))
    complaintRepo.update(
        action.complaint.copy(
            state = Complaint.State.NEW
        )
    )
    bot.execute(
        SendMessage(
            action.userId,
            "Номер вашей жалобы: `${action.complaint.id}`"
        ).parseMode(ParseMode.MarkdownV2)
    )
}
