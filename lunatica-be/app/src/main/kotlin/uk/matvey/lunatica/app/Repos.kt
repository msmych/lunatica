package uk.matvey.lunatica.app

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.newFixedThreadPoolContext
import uk.matvey.lunatica.account.AccountRepo
import uk.matvey.lunatica.complaint.ComplaintRepo
import uk.matvey.lunatica.message.MessageRepo

class Repos(hikariConfig: HikariConfig) {

    val ds = HikariDataSource(hikariConfig)
    @OptIn(DelicateCoroutinesApi::class)
    val dispatcher = newFixedThreadPoolContext(4, "data-source")

    val accountRepo = AccountRepo(ds, dispatcher)
    val complaintRepo = ComplaintRepo(ds, dispatcher)
    val messageRepo = MessageRepo(ds, dispatcher)
}
