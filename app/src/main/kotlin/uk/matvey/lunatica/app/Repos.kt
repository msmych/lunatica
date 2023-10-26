package uk.matvey.lunatica.app

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.newFixedThreadPoolContext
import uk.matvey.lunatica.complaints.ComplaintPgRepo
import uk.matvey.lunatica.complaints.messages.MessagePgRepo

class Repos(hikariConfig: HikariConfig) {

    val ds = HikariDataSource(hikariConfig)
    @OptIn(DelicateCoroutinesApi::class)
    val dsDispatcher = newFixedThreadPoolContext(4, "data-source")
    val complaintRepo = ComplaintPgRepo(ds, dsDispatcher)
    val messageRepo = MessagePgRepo(ds, dsDispatcher)
}
