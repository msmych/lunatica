package uk.matvey.lunatica.app.yabeda

import com.pengrad.telegrambot.TelegramBot
import com.pengrad.telegrambot.model.request.InlineKeyboardButton
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup
import com.pengrad.telegrambot.request.AnswerCallbackQuery
import com.pengrad.telegrambot.request.EditMessageText
import uk.matvey.lunatica.complaint.ComplaintService

suspend fun setComplaintType(
    complaintService: ComplaintService,
    action: YabedaAction.SetComplaintType,
    bot: TelegramBot
) {
    complaintService.updateComplaintType(action.complaint, action.type)
    bot.execute(
        EditMessageText(action.userId, action.messageId, "Подробно опишите нарушение:").replyMarkup(
            InlineKeyboardMarkup(*arrayOf<InlineKeyboardButton>())
        )
    )
    bot.execute(AnswerCallbackQuery(action.callbackQueryId))
}
