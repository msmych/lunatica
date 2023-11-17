package uk.matvey.lunatica.app

import com.zaxxer.hikari.HikariConfig
import mu.KotlinLogging
import uk.matvey.lunatica.app.yabeda.startYabedaBot

val log = KotlinLogging.logger("Lunatica App")

fun main() {
//    val repos = FbRepos(System.getenv("FB_PROJECT_ID"))
    val repos = PgRepos(HikariConfig("/configs/hikari-local.properties"))
    log.info { "Starting server" }
    createServer(repos.complaintRepo).start()
    startYabedaBot(repos.accountRepo, repos.complaintRepo, repos.messageRepo)
}
