package uk.matvey.lunatica.app.yabeda

import com.pengrad.telegrambot.TelegramBot
import com.pengrad.telegrambot.request.SendMessage
import uk.matvey.lunatica.message.Message
import uk.matvey.lunatica.message.MessageRepo

suspend fun sendComplaintMessage(
    action: YabedaAction.SendComplaintMessage,
    messageRepo: MessageRepo,
    bot: TelegramBot
) {
    val message = Message.complaintMessage(action.complaint.id, action.text)
    messageRepo.insert(message)
    bot.execute(SendMessage(action.userId, "Напишите адрес электронной почты для обратной связи:"))
}
