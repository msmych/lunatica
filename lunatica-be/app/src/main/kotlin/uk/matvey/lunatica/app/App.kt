package uk.matvey.lunatica.app

import com.zaxxer.hikari.HikariConfig
import mu.KotlinLogging
import uk.matvey.lunatica.app.yabeda.startYabedaBot

val log = KotlinLogging.logger("Lunatica App")

fun main() {
    val repos = Repos(HikariConfig("/configs/config-local-compose.properties"))
    val services = Services(repos)
    migrateDb(repos.ds)
    log.info { "Starting server" }
    val yabedaBot = startYabedaBot(services, repos)
    createServer(services, repos).start(wait = yabedaBot == null)
}
