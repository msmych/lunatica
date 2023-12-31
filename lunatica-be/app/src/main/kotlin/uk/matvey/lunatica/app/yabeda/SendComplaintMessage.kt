package uk.matvey.lunatica.app.yabeda

import com.pengrad.telegrambot.TelegramBot
import com.pengrad.telegrambot.request.SendMessage
import uk.matvey.lunatica.app.AppConfig
import uk.matvey.lunatica.app.yabeda.YabedaAction.SendComplaintMessage
import uk.matvey.lunatica.complaint.Complaint
import uk.matvey.lunatica.complaint.ComplaintService
import uk.matvey.lunatica.message.MessageService

suspend fun sendComplaintMessage(
    action: SendComplaintMessage,
    complaintService: ComplaintService,
    messageService: MessageService,
    bot: TelegramBot,
    config: AppConfig,
) {
    messageService.createMessage(action.account.id, action.complaint.id, action.text)
    if (action.account.email == null) {
        bot.execute(SendMessage(action.userId, "Напишите адрес электронной почты для обратной связи:"))
    } else {
        complaintService.updateComplaintState(action.complaint, Complaint.State.NEW)
        bot.sendComplaintLink(action.userId, action.complaint.id, config)
    }
}
