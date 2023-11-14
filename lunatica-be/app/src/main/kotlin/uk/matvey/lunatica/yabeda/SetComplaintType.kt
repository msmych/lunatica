package uk.matvey.lunatica.yabeda

import com.pengrad.telegrambot.TelegramBot
import com.pengrad.telegrambot.model.request.InlineKeyboardButton
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup
import com.pengrad.telegrambot.request.AnswerCallbackQuery
import com.pengrad.telegrambot.request.EditMessageText
import uk.matvey.lunatica.complaint.ComplaintRepo
import uk.matvey.lunatica.yabeda.YabedaAction.SetComplaintType

fun setComplaintType(
    complaintRepo: ComplaintRepo,
    action: SetComplaintType,
    bot: TelegramBot
) {
    complaintRepo.update(
        action.complaint.copy(
            type = action.type
        )
    )
    bot.execute(
        EditMessageText(action.userId, action.messageId, "Подробно опишите нарушение:").replyMarkup(
            InlineKeyboardMarkup(*arrayOf<InlineKeyboardButton>())
        )
    )
    bot.execute(AnswerCallbackQuery(action.callbackQueryId))
}
