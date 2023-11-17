package uk.matvey.lunatica.app

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.newFixedThreadPoolContext
import uk.matvey.lunatica.complaints.ComplaintPgRepo
import uk.matvey.lunatica.complaints.account.AccountPgRepo
import uk.matvey.lunatica.complaints.messages.MessagePgRepo

class PgRepos(hikariConfig: HikariConfig) {

    val ds = HikariDataSource(hikariConfig)
    @OptIn(DelicateCoroutinesApi::class)
    val dispatcher = newFixedThreadPoolContext(4, "data-source")

    val accountRepo = AccountPgRepo(ds, dispatcher)
    val complaintRepo = ComplaintPgRepo(ds, dispatcher)
    val messageRepo = MessagePgRepo(ds, dispatcher)
}
