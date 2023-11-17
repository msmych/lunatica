package uk.matvey.lunatica.complaints.account

import kotlinx.coroutines.CoroutineDispatcher
import uk.matvey.lunatica.pg.PgEntityRepo
import uk.matvey.lunatica.repo.RelCol.Num.Companion.numRel
import uk.matvey.lunatica.repo.RelCol.Text.Companion.textRel
import uk.matvey.lunatica.repo.RelCol.TimeStamp.Companion.timeStampRel
import uk.matvey.lunatica.repo.RelCol.Uuid
import uk.matvey.lunatica.repo.RelCol.Uuid.Companion.uuidRel
import uk.matvey.lunatica.repo.RelTab
import uk.matvey.lunatica.repo.RelTab.Companion.relTab
import java.sql.ResultSet
import java.util.UUID
import javax.sql.DataSource

class AccountPgRepo(ds: DataSource, dispatcher: CoroutineDispatcher) :
    PgEntityRepo<Uuid, Account>("accounts", ds, dispatcher), AccountRepo {
    override suspend fun findByTgChatId(tgChatId: Long): Account? {
        return selectStar("where tg_chat_id = ?", numRel(tgChatId)).singleOrNull()
    }

    override fun Account.toTableRecord(): RelTab {
        return relTab(
            linkedMapOf(
                "id" to uuidRel(this.id),
                "email" to textRel(this.email),
                "pass_hash" to textRel(this.passHash),
                "tg_chat_id" to numRel(this.tgChatId),
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
            this.getLong("tg_chat_id").takeUnless { this.wasNull() },
            this.getTimestamp("created_at").toInstant(),
            this.getTimestamp("updated_at").toInstant(),
        )
    }
}
