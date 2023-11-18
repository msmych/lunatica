package uk.matvey.lunatica.account

import com.google.cloud.firestore.Firestore
import uk.matvey.lunatica.fb.FbRepo
import java.time.Instant
import kotlin.coroutines.CoroutineContext

class AccountFbRepo(db: Firestore, dispatcher: CoroutineContext) : FbRepo<Account>("accounts", db, dispatcher),
    AccountRepo {
    override suspend fun findByEmail(email: String): Account? {
        TODO("Not yet implemented")
    }

    override suspend fun findByTgChatId(tgChatId: Long): Account {
        TODO("Not yet implemented")
    }

    override suspend fun insert(entity: Account) {
        TODO("Not yet implemented")
    }

    override suspend fun update(entity: Account): Instant? {
        TODO("Not yet implemented")
    }

    override suspend fun delete(entity: Account) {
        TODO("Not yet implemented")
    }

    override fun Account.toDoc(): Map<String, Any?> {
        TODO("Not yet implemented")
    }

    override fun Map<String, Any?>.toEntity(id: String): Account {
        TODO("Not yet implemented")
    }
}
