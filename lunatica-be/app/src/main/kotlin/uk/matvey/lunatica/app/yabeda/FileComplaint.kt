package uk.matvey.lunatica.app.yabeda

import com.pengrad.telegrambot.TelegramBot
import com.pengrad.telegrambot.model.request.InlineKeyboardButton
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup
import com.pengrad.telegrambot.request.SendMessage
import uk.matvey.lunatica.account.AccountService
import uk.matvey.lunatica.app.yabeda.YabedaAction.FileComplaint
import uk.matvey.lunatica.complaint.ComplaintService
import uk.matvey.lunatica.complaint.ComplaintSetup.PROBLEM_COUNTRIES

suspend fun fileComplaint(
    action: FileComplaint,
    accountService: AccountService,
    complaintService: ComplaintService,
    bot: TelegramBot
) {
    val account = accountService.ensureTgAccount(action.userId, action.username)
    complaintService.createDraftComplaint(account.id)
    bot.execute(
        SendMessage(action.userId, "В какой стране произошло нарушение?")
            .replyMarkup(InlineKeyboardMarkup().apply {
                PROBLEM_COUNTRIES.entries.chunked(3).map { countries ->
                    addRow(
                        *countries.map { (code, info) ->
                            InlineKeyboardButton(info.label()).callbackData(code.name)
                        }.toTypedArray()
                    )
                }
            })
    )
}
