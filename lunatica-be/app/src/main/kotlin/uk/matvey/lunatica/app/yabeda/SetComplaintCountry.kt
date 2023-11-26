package uk.matvey.lunatica.app.yabeda

import com.pengrad.telegrambot.TelegramBot
import com.pengrad.telegrambot.model.request.InlineKeyboardButton
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup
import com.pengrad.telegrambot.request.AnswerCallbackQuery
import com.pengrad.telegrambot.request.EditMessageText
import uk.matvey.lunatica.app.yabeda.YabedaAction.SetComplaintCountry
import uk.matvey.lunatica.complaint.ComplaintService
import uk.matvey.lunatica.complaint.ComplaintSetup.COMPLAINTS_TYPES

suspend fun setComplaintCountry(
    complaintService: ComplaintService,
    action: SetComplaintCountry,
    bot: TelegramBot
) {
    complaintService.updateComplaintProblemCountry(action.complaint, action.country)
    bot.execute(
        EditMessageText(
            action.userId,
            action.messageId,
            "Какое нарушение произошло?"
        )
            .replyMarkup(InlineKeyboardMarkup().apply {
                COMPLAINTS_TYPES.map { (code, info) ->
                    addRow(
                        InlineKeyboardButton(info.label()).callbackData(code.name)
                    )
                }
            })
    )
    bot.execute(AnswerCallbackQuery(action.callbackQueryId))
}
