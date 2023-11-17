package uk.matvey.lunatica.yabeda

import com.pengrad.telegrambot.TelegramBot
import com.pengrad.telegrambot.model.request.ParseMode.MarkdownV2
import com.pengrad.telegrambot.request.SendMessage
import uk.matvey.lunatica.account.AccountRepo
import uk.matvey.lunatica.complaint.Complaint
import uk.matvey.lunatica.complaint.ComplaintRepo

fun setContactEmail(
    accountRepo: AccountRepo,
    complaintRepo: ComplaintRepo,
    action: YabedaAction.SetContactEmail,
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
        ).parseMode(MarkdownV2)
    )
}
