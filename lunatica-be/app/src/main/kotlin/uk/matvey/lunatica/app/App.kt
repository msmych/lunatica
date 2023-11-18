package uk.matvey.lunatica.app

import com.zaxxer.hikari.HikariConfig
import mu.KotlinLogging
import uk.matvey.lunatica.app.yabeda.startYabedaBot

val log = KotlinLogging.logger("Lunatica App")

fun main() {
    val repos = Repos(HikariConfig("/configs/hikari-local.properties"))
    val services = Services(repos)
    log.info { "Starting server" }
    createServer(services, repos).start()
    startYabedaBot(services, repos)
}
