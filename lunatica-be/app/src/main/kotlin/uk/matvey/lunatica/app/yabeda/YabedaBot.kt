package uk.matvey.lunatica.app.yabeda

import com.pengrad.telegrambot.TelegramBot
import com.pengrad.telegrambot.UpdatesListener.CONFIRMED_UPDATES_ALL
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.newSingleThreadContext
import uk.matvey.lunatica.app.Repos
import uk.matvey.lunatica.app.Services
import uk.matvey.lunatica.app.yabeda.YabedaAction.FileComplaint
import uk.matvey.lunatica.app.yabeda.YabedaAction.SendComplaintMessage
import uk.matvey.lunatica.app.yabeda.YabedaAction.SetAccountEmail
import uk.matvey.lunatica.app.yabeda.YabedaAction.SetComplaintCountry
import uk.matvey.lunatica.app.yabeda.YabedaAction.SetComplaintType

@OptIn(DelicateCoroutinesApi::class)
fun startYabedaBot(
    services: Services,
    repos: Repos,
) {
    val bot = TelegramBot(System.getenv("YABEDA_BOT_TOKEN"))
    val actionSelector = YabedaActionSelector(repos.accountRepo, repos.complaintRepo, repos.messageRepo)
    val yabedaScope = CoroutineScope(newSingleThreadContext("yabeda-bot"))
    bot.setUpdatesListener { updates ->
        updates.forEach { update ->
            yabedaScope.launch {
                when (val action = actionSelector.select(update)) {
                    is FileComplaint -> fileComplaint(action, services.accountService, services.complaintService, bot)
                    is SetComplaintCountry -> setComplaintCountry(services.complaintService, action, bot)
                    is SetComplaintType -> setComplaintType(services.complaintService, action, bot)
                    is SendComplaintMessage -> sendComplaintMessage(action, services.messageService, bot)
                    is SetAccountEmail -> setAccountEmail(services.accountService, services.complaintService, action, bot)
                    is YabedaAction.Noop -> {}
                }
            }
        }
        CONFIRMED_UPDATES_ALL
    }
}
