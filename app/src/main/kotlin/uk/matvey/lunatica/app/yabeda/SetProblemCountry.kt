package uk.matvey.lunatica.app.yabeda

import com.pengrad.telegrambot.TelegramBot
import com.pengrad.telegrambot.model.Update
import com.pengrad.telegrambot.model.request.InlineKeyboardButton
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup
import com.pengrad.telegrambot.request.AnswerCallbackQuery
import com.pengrad.telegrambot.request.EditMessageText
import uk.matvey.lunatica.complaints.ComplaintRepo

suspend fun setProblemCountry(
    complaintRepo: ComplaintRepo,
    action: YabedaAction.SetProblemCountry,
    bot: TelegramBot,
    update: Update
) {
    complaintRepo.update(
        action.complaint.copy(
            problemCountry = action.country
        )
    )
    bot.execute(
        EditMessageText(
            action.userId,
            action.messageId,
            "Подробно опишите нарушение:"
        ).replyMarkup(
            InlineKeyboardMarkup(*arrayOf<InlineKeyboardButton>())
        )
    )
    bot.execute(AnswerCallbackQuery(update.callbackQuery().id()))
}
