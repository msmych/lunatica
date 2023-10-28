package uk.matvey.lunatica.app.yabeda

import com.pengrad.telegrambot.TelegramBot
import com.pengrad.telegrambot.UpdatesListener.CONFIRMED_UPDATES_ALL
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.newSingleThreadContext
import mu.KotlinLogging
import uk.matvey.lunatica.app.yabeda.YabedaAction.FileComplaint
import uk.matvey.lunatica.app.yabeda.YabedaAction.SendComplaintMessage
import uk.matvey.lunatica.app.yabeda.YabedaAction.SetContactEmail
import uk.matvey.lunatica.app.yabeda.YabedaAction.SetProblemCountry
import uk.matvey.lunatica.complaints.ComplaintRepo
import uk.matvey.lunatica.complaints.messages.MessageRepo

private val log = KotlinLogging.logger("YabedaBot")

@OptIn(DelicateCoroutinesApi::class)
fun startYabedaBot(complaintRepo: ComplaintRepo, messageRepo: MessageRepo) {
    val bot = TelegramBot(System.getenv("YABEDA_BOT_TOKEN"))
    val actionSelector = YabedaActionSelector(complaintRepo, messageRepo, bot)
    val yabedaScope = CoroutineScope(newSingleThreadContext("yabeda-bot"))
    bot.setUpdatesListener({ updates ->
        updates.forEach { update ->
            yabedaScope.launch {
                when (val action = actionSelector.select(update)) {
                    is FileComplaint -> fileComplaint(action, complaintRepo, bot)

                    is SetProblemCountry -> setProblemCountry(complaintRepo, action, bot)

                    is SendComplaintMessage -> sendComplaintMessage(action, messageRepo, bot)

                    is SetContactEmail -> setContactEmail(complaintRepo, action, bot)

                    YabedaAction.Noop -> {}
                }
            }
        }
        CONFIRMED_UPDATES_ALL
    }, { e -> log.error(e) {} })
}
