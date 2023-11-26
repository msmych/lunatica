package uk.matvey.lunatica.pg

import kotlinx.coroutines.CoroutineDispatcher
import mu.KotlinLogging
import uk.matvey.lunatica.pg.RelCol.TimeStamp.Companion.timeStampRel
import java.time.Instant
import javax.sql.DataSource

abstract class PgEntityRepo<ID : RelCol, E : Entity<ID>>(
    tableName: String,
    ds: DataSource,
    dispatcher: CoroutineDispatcher
) : PgRepo<E>(tableName, ds, dispatcher) {

    private val log = KotlinLogging.logger {}

    suspend fun update(entity: E): Instant? {
        return withConnection { conn ->
            val columns = entity.toTableRecord().columns
                .filterKeys { it != "id" }
                .filterKeys { it != "updated_at" }
            val columnsSetValues = columns.keys.joinToString(", ") { "$it = ?" }
            conn.prepareStatement("update $tableName set $columnsSetValues, updated_at = ? where id = ? and updated_at = ?")
                .use { statement ->
                    columns.values.forEachIndexed { i, v -> setQueryParam(statement, i + 1, v) }
                    val updatedAt = Instant.now()
                    setQueryParam(statement, columns.size + 1, timeStampRel(updatedAt))
                    setQueryParam(statement, columns.size + 2, entity.idRel())
                    setQueryParam(statement, columns.size + 3, entity.updatedAtRel())
                    try {
                        val updatedCount = statement.executeUpdate()
                        updatedAt.takeIf { updatedCount > 0 }
                    } catch (e: Exception) {
                        log.error(e) {}
                        null
                    }
                }
        }
    }

    suspend fun delete(entity: E) {
        return withConnection { conn ->
            conn.prepareStatement("delete from $tableName where id = ? and updated_at = ?").use { statement ->
                setQueryParam(statement, 1, entity.idRel())
                setQueryParam(statement, 2, entity.updatedAtRel())
                try {
                    statement.executeUpdate()
                } catch (e: Exception) {
                    log.error(e) {}
                }
            }
        }
    }
}
