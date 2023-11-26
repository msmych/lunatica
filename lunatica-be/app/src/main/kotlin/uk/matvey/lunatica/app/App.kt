package uk.matvey.lunatica.app

import com.amazonaws.services.s3.AmazonS3ClientBuilder
import com.sksamuel.hoplite.ConfigLoaderBuilder
import com.sksamuel.hoplite.addResourceSource
import com.zaxxer.hikari.HikariConfig
import mu.KotlinLogging
import uk.matvey.lunatica.app.yabeda.startYabedaBot
import uk.matvey.lunatica.complaint.InMemoryFileStorage
import uk.matvey.lunatica.complaint.S3FileStorage
import java.net.URI

val log = KotlinLogging.logger("Lunatica App")

fun main() {
    val profile = System.getenv("LUNATICA_PROFILE")
    val config = loadConfigs(profile)
    val repos = Repos(config.db.hikariConfig())
    val services = Services(repos)
    migrateDb(repos.ds)
    log.info { "Starting server" }
    val yabedaBot = startYabedaBot(services, repos, config)
    val fileStorage = if (profile == "aws") {
        S3FileStorage(AmazonS3ClientBuilder.standard().build())
    } else {
        InMemoryFileStorage()
    }
    createServer(services, repos, fileStorage, yabedaBot).start(wait = yabedaBot == null)
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
