package uk.matvey.lunatica.app

import mu.KotlinLogging
import uk.matvey.lunatica.app.yabeda.startYabedaBot

val log = KotlinLogging.logger("Lunatica App")

fun main() {
    val repos = FbRepos(System.getenv("FB_PROJECT_ID"))
//    val repos = Repos(HikariConfig("/configs/hikari-local.properties"))
    log.info { "Starting server" }
    createServer(repos.complaintRepo).start()
    startYabedaBot(repos.complaintRepo, repos.messageRepo)
}
