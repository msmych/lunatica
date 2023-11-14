package uk.matvey.lunatica.account

import com.google.cloud.firestore.Firestore
import org.springframework.stereotype.Component
import java.time.Instant
import java.util.UUID

@Component
class AccountRepo(private val db: Firestore) {
    fun add(account: Account) {
        db.collection("accounts").document(account.id.toString()).set(account.toDoc())
    }

    fun get(id: UUID): Account {
        return db.collection("accounts").document(id.toString()).get().get()
            .let { fromDoc(id.toString(), requireNotNull(it.data)) }
    }

    fun update(account: Account) {
        db.collection("accounts").document(account.id.toString())
            .set(account.toDoc())
    }

    fun getByTgChatId(tgChatId: Long): Account {
        return db.collection("accounts").whereEqualTo("tgChatId", tgChatId).get().get().single()
            .let { fromDoc(it.id, requireNotNull(it.data)) }
    }

    companion object {
        private fun Account.toDoc(): Map<String, Any> {
            return listOfNotNull(
                "email" to this.email,
                "passHash" to this.passHash,
                "createdAt" to this.createdAt.toString(),
                "updatedAt" to this.updatedAt.toString(),
                this.tgChatId?.let { "tgChatId" to it },
            ).toMap()
        }

        private fun fromDoc(id: String, doc: Map<String, Any>): Account {
            return Account(
                UUID.fromString(id),
                doc.getValue("email").toString(),
                doc.getValue("passHash").toString(),
                doc["tgChatId"] as? Long,
                Instant.parse(doc.getValue("createdAt").toString()),
                Instant.parse(doc.getValue("updatedAt").toString())
            )
        }
    }
}
