package uk.matvey.lunatica.app.yabeda

import com.pengrad.telegrambot.TelegramBot
import com.pengrad.telegrambot.request.SendMessage
import uk.matvey.lunatica.message.MessageService

suspend fun sendComplaintMessage(
    action: YabedaAction.SendComplaintMessage,
    messageService: MessageService,
    bot: TelegramBot
) {
    messageService.createMessage(action.complaint.id, action.text)
    bot.execute(SendMessage(action.userId, "Напишите адрес электронной почты для обратной связи:"))
}
