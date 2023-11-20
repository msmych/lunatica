package uk.matvey.lunatica.app

import org.flywaydb.core.Flyway
import javax.sql.DataSource

fun migrateDb(ds: DataSource) {
    val result = Flyway.configure()
        .dataSource(ds)
        .schemas("public")
        .locations("classpath:db/migration")
        .defaultSchema("public")
        .createSchemas(true)
        .load()
        .migrate()
}
