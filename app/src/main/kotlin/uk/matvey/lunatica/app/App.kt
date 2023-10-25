package uk.matvey.lunatica.app

import com.zaxxer.hikari.HikariConfig
import mu.KotlinLogging
import uk.matvey.lunatica.app.yabeda.startYabedaBot

val log = KotlinLogging.logger("Lunatica App")

fun main() {
    log.info { "Starting server" }
    val repos = Repos(HikariConfig("/configs/hikari-local.properties"))
    createServer(repos).start()
    startYabedaBot(repos.complaintRepo, repos.messageRepo)
}
