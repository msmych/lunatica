package uk.matvey.lunatica.app.yabeda

import com.pengrad.telegrambot.TelegramBot
import com.pengrad.telegrambot.model.request.InlineKeyboardButton
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup
import com.pengrad.telegrambot.model.request.ParseMode.MarkdownV2
import com.pengrad.telegrambot.request.SendMessage
import mu.KotlinLogging
import uk.matvey.lunatica.app.AppConfig
import java.util.UUID

fun TelegramBot.sendComplaintLink(userId: Long, complaintId: UUID, config: AppConfig) {
    if (config.baseUrl.authority.contains("localhost")) {
        execute(
            SendMessage(
                userId,
                "Принято\\! Ссылка на ваше обращение:\n${config.baseUrl}/complaints/$complaintId"
                    .replace("-", "\\-")
            )
                .parseMode(MarkdownV2)
        )
    } else {
        execute(
            SendMessage(
                userId,
                "Принято\\! Ссылка на ваше обращение:"
            ).parseMode(MarkdownV2)
                .replyMarkup(
                    InlineKeyboardMarkup().addRow(
                        InlineKeyboardButton("Перейти")
                            .url("${config.baseUrl}/complaints/$complaintId")
                    )
                )
        )
    }
}
