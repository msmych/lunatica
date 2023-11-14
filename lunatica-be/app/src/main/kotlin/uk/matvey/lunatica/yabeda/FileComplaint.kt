package uk.matvey.lunatica.yabeda

import com.pengrad.telegrambot.TelegramBot
import com.pengrad.telegrambot.model.request.InlineKeyboardButton
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup
import com.pengrad.telegrambot.request.SendMessage
import uk.matvey.lunatica.account.AccountRepo
import uk.matvey.lunatica.complaint.Complaint
import uk.matvey.lunatica.complaint.ComplaintRepo
import uk.matvey.lunatica.yabeda.YabedaSetup.PROBLEM_COUNTRIES

fun fileComplaint(
    action: YabedaAction.FileComplaint,
    accountRepo: AccountRepo,
    complaintRepo: ComplaintRepo,
    bot: TelegramBot
) {
    val account = accountRepo.getByTgChatId(action.userId)
    val complaint = Complaint.draft(account.id)
    complaintRepo.add(complaint)
    bot.execute(
        SendMessage(action.userId, "В какой стране произошло нарушение?")
            .replyMarkup(InlineKeyboardMarkup().apply {
                PROBLEM_COUNTRIES.map { (code, info) ->
                    addRow(
                        InlineKeyboardButton(info.label()).callbackData(code.name)
                    )
                }
            })
    )
}
