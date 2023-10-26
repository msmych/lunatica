package uk.matvey.lunatica.app.yabeda

import com.neovisionaries.i18n.CountryCode
import com.pengrad.telegrambot.TelegramBot
import com.pengrad.telegrambot.UpdatesListener.CONFIRMED_UPDATES_ALL
import com.pengrad.telegrambot.model.request.InlineKeyboardButton
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup
import com.pengrad.telegrambot.model.request.ParseMode.MarkdownV2
import com.pengrad.telegrambot.request.AnswerCallbackQuery
import com.pengrad.telegrambot.request.EditMessageText
import com.pengrad.telegrambot.request.SendMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.newSingleThreadContext
import uk.matvey.lunatica.app.yabeda.YabedaAction.FileComplaint
import uk.matvey.lunatica.app.yabeda.YabedaAction.SendComplaintMessage
import uk.matvey.lunatica.app.yabeda.YabedaAction.SetContactEmail
import uk.matvey.lunatica.app.yabeda.YabedaAction.SetProblemCountry
import uk.matvey.lunatica.complaints.Complaint
import uk.matvey.lunatica.complaints.ComplaintRepo
import uk.matvey.lunatica.complaints.ComplaintSetup.PROBLEM_COUNTRIES
import uk.matvey.lunatica.complaints.messages.Message
import uk.matvey.lunatica.complaints.messages.MessageRepo

@OptIn(DelicateCoroutinesApi::class)
fun startYabedaBot(complaintRepo: ComplaintRepo, messageRepo: MessageRepo) {
    val bot = TelegramBot(System.getenv("YABEDA_BOT_TOKEN"))
    val actionSelector = YabedaActionSelector(complaintRepo, messageRepo, bot)
    val yabedaScope = CoroutineScope(newSingleThreadContext("yabeda-bot"))
    bot.setUpdatesListener { updates ->
        updates.forEach { update ->
            yabedaScope.launch {
                when (val action = actionSelector.select(update)) {
                    is FileComplaint -> {
                        val complaint = Complaint.draft(
                            mapOf(
                                "tgUserId" to action.userId.toString()
                            )
                        )
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

                    is SetProblemCountry -> {
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

                    is SendComplaintMessage -> {
                        val message = Message.complaintMessage(action.complaint.id, action.text)
                        messageRepo.insert(message)
                        bot.execute(SendMessage(action.userId, "Напишите адрес электронной почты для обратной связи:"))
                    }

                    is SetContactEmail -> {
                        complaintRepo.update(
                            action.complaint.copy(
                                contactDetails = action.complaint.contactDetails + mapOf("email" to action.email),
                                state = Complaint.State.NEW
                            )
                        )
                        bot.execute(
                            SendMessage(
                                action.userId,
                                "Номер вашей жалобы: `${action.complaint.id}`"
                            ).parseMode(MarkdownV2)
                        )
                    }

                    YabedaAction.Noop -> {}
                }
            }
        }
        CONFIRMED_UPDATES_ALL
    }
}
