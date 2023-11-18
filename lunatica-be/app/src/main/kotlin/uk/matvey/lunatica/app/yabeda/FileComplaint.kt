package uk.matvey.lunatica.app.yabeda

import com.pengrad.telegrambot.TelegramBot
import com.pengrad.telegrambot.model.request.InlineKeyboardButton
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup
import com.pengrad.telegrambot.request.SendMessage
import uk.matvey.lunatica.account.Account
import uk.matvey.lunatica.account.AccountRepo
import uk.matvey.lunatica.app.yabeda.YabedaAction.FileComplaint
import uk.matvey.lunatica.complaint.Complaint
import uk.matvey.lunatica.complaint.ComplaintRepo
import uk.matvey.lunatica.complaint.ComplaintSetup.PROBLEM_COUNTRIES

suspend fun fileComplaint(
    action: FileComplaint,
    accountRepo: AccountRepo,
    complaintRepo: ComplaintRepo,
    bot: TelegramBot
) {
    val account = accountRepo.findByTgChatId(action.userId) ?: Account.tgAccount(action.userId).also { accountRepo.insert(it) }
    val complaint = Complaint.draft(account.id)
    complaintRepo.insert(complaint)
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
