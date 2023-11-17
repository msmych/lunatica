package uk.matvey.lunatica.yabeda

import com.pengrad.telegrambot.TelegramBot
import com.pengrad.telegrambot.UpdatesListener.CONFIRMED_UPDATES_ALL
import jakarta.annotation.PostConstruct
import org.springframework.stereotype.Component
import uk.matvey.lunatica.account.AccountRepo
import uk.matvey.lunatica.complaint.ComplaintRepo
import uk.matvey.lunatica.message.MessageRepo
import uk.matvey.lunatica.yabeda.YabedaAction.FileComplaint
import uk.matvey.lunatica.yabeda.YabedaAction.SendComplaintMessage
import uk.matvey.lunatica.yabeda.YabedaAction.SetComplaintCountry
import uk.matvey.lunatica.yabeda.YabedaAction.SetComplaintType
import uk.matvey.lunatica.yabeda.YabedaAction.SetContactEmail

@Component
class YabedaBot(
    private val accountRepo: AccountRepo,
    private val complaintRepo: ComplaintRepo,
    private val messageRepo: MessageRepo,
    private val holder: TelegramBotHolder,
) {

    @PostConstruct
    fun start() {
        holder.telegramBot?.let { bot ->
            val actionSelector = YabedaActionSelector(accountRepo, complaintRepo, messageRepo)
            bot.setUpdatesListener { updates ->
                updates.forEach { update ->
                    when (val action = actionSelector.select(update)) {
                        is FileComplaint -> fileComplaint(action, accountRepo, complaintRepo, bot)
                        is SetComplaintCountry -> setComplaintCountry(complaintRepo, action, bot)
                        is SetComplaintType -> setComplaintType(complaintRepo, action, bot)
                        is SendComplaintMessage -> sendComplaintMessage(action, messageRepo, bot)
                        is SetContactEmail -> setContactEmail(accountRepo, complaintRepo, action, bot)
                        is YabedaAction.Noop -> {}
                    }
                }
                CONFIRMED_UPDATES_ALL
            }
        }
    }

    class TelegramBotHolder(val telegramBot: TelegramBot?)
}
