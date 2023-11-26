package uk.matvey.lunatica.account

import kotlinx.coroutines.CoroutineDispatcher
import uk.matvey.lunatica.pg.PgEntityRepo
import uk.matvey.lunatica.pg.RelCol.Num.Companion.numRel
import uk.matvey.lunatica.pg.RelCol.Text.Companion.textRel
import uk.matvey.lunatica.pg.RelCol.TextArray.Companion.textArrayRel
import uk.matvey.lunatica.pg.RelCol.TimeStamp.Companion.timeStampRel
import uk.matvey.lunatica.pg.RelCol.Uuid
import uk.matvey.lunatica.pg.RelCol.Uuid.Companion.uuidRel
import uk.matvey.lunatica.pg.RelTab
import uk.matvey.lunatica.pg.RelTab.Companion.relTab
import java.sql.ResultSet
import java.util.UUID
import javax.sql.DataSource

class AccountRepo(ds: DataSource, dispatcher: CoroutineDispatcher) :
    PgEntityRepo<Uuid, Account>("accounts", ds, dispatcher) {

    suspend fun get(id: UUID): Account {
        return selectStar("where id = ?", uuidRel(id)).single()
    }

    suspend fun list(role: Account.Role?): List<Account> {
        return selectStar(role?.let { "where '$it' = any(roles)" } ?: "")
    }

    suspend fun findByEmail(email: String): Account? {
        return selectStar("where email = ?", textRel(email)).singleOrNull()
    }

    suspend fun findByTgChatId(tgChatId: Long): Account? {
        return selectStar("where tg_chat_id = ?", numRel(tgChatId)).singleOrNull()
    }

    override fun Account.toTableRecord(): RelTab {
        return relTab(
            linkedMapOf(
                "id" to uuidRel(this.id),
                "email" to textRel(this.email),
                "pass_hash" to textRel(this.passHash),
                "name" to textRel(this.name),
                "tg_chat_id" to numRel(this.tgChatId),
                "roles" to textArrayRel(this.roles.map { it.toString() }),
                "created_at" to timeStampRel(this.createdAt),
                "updated_at" to timeStampRel(this.createdAt),
            )
        )
    }

    override fun ResultSet.toEntity(): Account {
        return Account(
            UUID.fromString(this.getString("id")),
            this.getString("email"),
            this.getString("pass_hash"),
            this.getString("name"),
            (this.getArray("roles").array as Array<String>).map { Account.Role.valueOf(it) },
            this.getLong("tg_chat_id").takeUnless { this.wasNull() },
            this.getTimestamp("created_at").toInstant(),
            this.getTimestamp("updated_at").toInstant(),
        )
    }
}
