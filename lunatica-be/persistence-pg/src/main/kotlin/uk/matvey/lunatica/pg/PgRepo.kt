package uk.matvey.lunatica.pg

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.postgresql.util.PGobject
import uk.matvey.lunatica.pg.RelCol.Date
import uk.matvey.lunatica.pg.RelCol.Jsonb
import uk.matvey.lunatica.pg.RelCol.Num
import uk.matvey.lunatica.pg.RelCol.Text
import uk.matvey.lunatica.pg.RelCol.TextArray
import uk.matvey.lunatica.pg.RelCol.TimeStamp
import uk.matvey.lunatica.pg.RelCol.Uuid
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.Timestamp
import java.sql.Types.INTEGER
import java.time.ZoneOffset.UTC
import javax.sql.DataSource

abstract class PgRepo<E>(
    protected val tableName: String,
    private val ds: DataSource,
    private val dispatcher: CoroutineDispatcher
) {

    suspend fun insert(entity: E) {
        return withConnection { conn ->
            val tableRecord = entity.toTableRecord()
            val columnNames = tableRecord.columnNames()
            val columns = columnNames.joinToString(", ")
            val questions = columnNames.joinToString(", ") { "?" }
            val query = "insert into $tableName ($columns) values ($questions)"
            conn.prepareStatement(query).use { statement ->
                tableRecord.columns.values.forEachIndexed { i, v ->
                    setQueryParam(statement, i + 1, v)
                }
                statement.executeUpdate()
            }
        }
    }

    suspend fun selectStar(where: String, vararg params: RelCol): List<E> {
        return withConnection { conn ->
            conn.prepareStatement("select * from $tableName $where").use { statement ->
                params.forEachIndexed { i, v ->
                    setQueryParam(statement, i + 1, v)
                }
                val resultSet = statement.executeQuery()
                val result = mutableListOf<E>()
                while (resultSet.next()) {
                    result += resultSet.toEntity()
                }
                result
            }
        }
    }

    protected fun setQueryParam(statement: PreparedStatement, i: Int, value: RelCol) {
        when (value) {
            is Uuid -> statement.setObject(i, value.value)
            is Text -> statement.setString(i, value.value)
            is Num -> value.value?.let { statement.setLong(i, it) } ?: statement.setNull(i, INTEGER)
            is Date -> {
                statement.setDate(
                    i,
                    value.value?.atStartOfDay()?.toInstant(UTC)?.toEpochMilli()?.let { java.sql.Date(it) }
                )
            }

            is TimeStamp -> statement.setTimestamp(i, Timestamp.from(value.value))
            is TextArray -> statement.setArray(i, statement.connection.createArrayOf("text", value.value.toTypedArray()))
            is Jsonb -> statement.setObject(i, PGobject().apply {
                this.type = "jsonb"
                this.value = Json.encodeToString(value.value)
            })
        }
    }

    abstract fun E.toTableRecord(): RelTab

    abstract fun ResultSet.toEntity(): E

    protected suspend fun <R> withConnection(block: (Connection) -> R): R {
        return withContext(dispatcher) {
            ds.connection.use(block)
        }
    }
}
