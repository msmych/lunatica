package uk.matvey.lunatica.app

import com.sksamuel.hoplite.ConfigLoaderBuilder
import com.sksamuel.hoplite.addResourceSource
import com.zaxxer.hikari.HikariConfig
import mu.KotlinLogging
import uk.matvey.lunatica.app.yabeda.startYabedaBot

val log = KotlinLogging.logger("Lunatica App")

fun main() {
    val configs = loadConfigs(System.getenv("LUNATICA_PROFILE"))
    val repos = Repos(configs.db.hikariConfig())
    val services = Services(repos)
    migrateDb(repos.ds)
    log.info { "Starting server" }
    val yabedaBot = startYabedaBot(services, repos)
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
