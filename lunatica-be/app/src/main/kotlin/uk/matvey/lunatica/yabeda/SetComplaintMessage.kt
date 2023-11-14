package uk.matvey.lunatica.yabeda

import com.pengrad.telegrambot.TelegramBot
import com.pengrad.telegrambot.request.SendMessage
import uk.matvey.lunatica.message.Message
import uk.matvey.lunatica.message.MessageRepo

fun sendComplaintMessage(
    action: YabedaAction.SendComplaintMessage,
    messageRepo: MessageRepo,
    bot: TelegramBot
) {
    val message = Message.complaintMessage(action.complaint.id, action.text)
    messageRepo.add(message)
    bot.execute(SendMessage(action.userId, "Напишите адрес электронной почты для обратной связи:"))
}
