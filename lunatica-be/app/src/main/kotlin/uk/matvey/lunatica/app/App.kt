package uk.matvey.lunatica.app

import com.sksamuel.hoplite.ConfigLoaderBuilder
import com.sksamuel.hoplite.addResourceSource
import com.zaxxer.hikari.HikariConfig
import mu.KotlinLogging
import uk.matvey.lunatica.app.yabeda.startYabedaBot
import java.net.URI

val log = KotlinLogging.logger("Lunatica App")

fun main() {
    val config = loadConfigs(System.getenv("LUNATICA_PROFILE"))
    val repos = Repos(config.db.hikariConfig())
    val services = Services(repos)
    migrateDb(repos.ds)
    log.info { "Starting server" }
    val yabedaBot = startYabedaBot(services, repos, config)
    createServer(services, repos).start(wait = yabedaBot == null)
}

fun loadConfigs(profile: String): AppConfig {
    return ConfigLoaderBuilder.default()
        .addResourceSource("/configs/configs-$profile.yaml")
        .build()
        .loadConfigOrThrow<AppConfig>()
}

data class AppConfig(
    val db: DbConfig,
    val baseUrl: URI,
) {
    data class DbConfig(
        val jdbcUrl: String,
        val username: String,
        val password: String,
        val driverClassName: String,
    ) {
        fun hikariConfig(): HikariConfig {
            val hikariConfig = HikariConfig()
            hikariConfig.jdbcUrl = jdbcUrl
            hikariConfig.username = username
            hikariConfig.password = password
            hikariConfig.driverClassName = driverClassName
            return hikariConfig
        }
    }
}
