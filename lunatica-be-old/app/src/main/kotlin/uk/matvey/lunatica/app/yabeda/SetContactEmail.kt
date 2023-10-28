package uk.matvey.lunatica.app.yabeda

import com.pengrad.telegrambot.TelegramBot
import com.pengrad.telegrambot.model.request.ParseMode
import com.pengrad.telegrambot.request.SendMessage
import uk.matvey.lunatica.complaints.Complaint
import uk.matvey.lunatica.complaints.ComplaintRepo

suspend fun setContactEmail(
    complaintRepo: ComplaintRepo,
    action: YabedaAction.SetContactEmail,
    bot: TelegramBot
) {
    complaintRepo.update(
        action.complaint.copy(
            contactDetails = action.complaint.contactDetails + mapOf("email" to action.email),
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
