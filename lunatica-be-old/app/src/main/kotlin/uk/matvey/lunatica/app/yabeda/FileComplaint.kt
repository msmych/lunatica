package uk.matvey.lunatica.app.yabeda

import com.neovisionaries.i18n.CountryCode
import com.pengrad.telegrambot.TelegramBot
import com.pengrad.telegrambot.model.request.InlineKeyboardButton
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup
import com.pengrad.telegrambot.request.SendMessage
import uk.matvey.lunatica.complaints.Complaint
import uk.matvey.lunatica.complaints.ComplaintRepo
import uk.matvey.lunatica.complaints.ComplaintSetup.PROBLEM_COUNTRIES

suspend fun fileComplaint(
    action: YabedaAction.FileComplaint,
    complaintRepo: ComplaintRepo,
    bot: TelegramBot
) {
    val complaint = Complaint.draft(
        mapOf(
            "tgUserId" to action.userId.toString()
        )
    )
    complaintRepo.findAllDraftByTgUserId(action.userId).forEach { complaintRepo.delete(it) }
    complaintRepo.insert(complaint)
    bot.execute(
        SendMessage(action.userId, "В какой стране произошло нарушение?")
            .replyMarkup(InlineKeyboardMarkup().apply {
                PROBLEM_COUNTRIES.map { (code, info) ->
                    addRow(
                        InlineKeyboardButton("${info.emoji} ${info.nameRu}").callbackData(code.name)
                    )
                }
            }
                .addRow(InlineKeyboardButton("Другая страна").callbackData(CountryCode.UNDEFINED.name)))
    )
}
