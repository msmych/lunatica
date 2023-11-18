package uk.matvey.lunatica.app.yabeda

import com.pengrad.telegrambot.TelegramBot
import com.pengrad.telegrambot.model.request.InlineKeyboardButton
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup
import com.pengrad.telegrambot.request.AnswerCallbackQuery
import com.pengrad.telegrambot.request.EditMessageText
import uk.matvey.lunatica.app.yabeda.YabedaAction.SetComplaintCountry
import uk.matvey.lunatica.complaint.Complaint
import uk.matvey.lunatica.complaint.ComplaintRepo
import uk.matvey.lunatica.complaint.ComplaintSetup.toTgLabel

suspend fun setComplaintCountry(
    complaintRepo: ComplaintRepo,
    action: SetComplaintCountry,
    bot: TelegramBot
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
            "Выберите тип учреждения, где произошло нарушение"
        )
            .replyMarkup(InlineKeyboardMarkup().apply {
                Complaint.Type.entries.map { state ->
                    addRow(
                        InlineKeyboardButton(state.toTgLabel().label()).callbackData(state.name)
                    )
                }
            })
    )
    bot.execute(AnswerCallbackQuery(action.callbackQueryId))
}
