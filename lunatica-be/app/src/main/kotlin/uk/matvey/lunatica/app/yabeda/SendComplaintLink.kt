package uk.matvey.lunatica.app.yabeda

import com.pengrad.telegrambot.TelegramBot
import com.pengrad.telegrambot.model.request.ParseMode.MarkdownV2
import com.pengrad.telegrambot.request.SendMessage
import uk.matvey.lunatica.app.AppConfig
import java.util.UUID

fun TelegramBot.sendComplaintLink(userId: Long, complaintId: UUID, config: AppConfig) {
    execute(
        SendMessage(userId,
            "Ссылка на вашу жалобу: ${config.baseUrl}/complaints/$complaintId"
        ).parseMode(MarkdownV2)
    )
}
