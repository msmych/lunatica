package uk.matvey.lunatica.message

import com.google.cloud.firestore.Firestore
import uk.matvey.lunatica.fb.FbRepo
import java.time.Instant
import java.util.UUID
import kotlin.coroutines.CoroutineContext

class MessageFbRepo(db: Firestore, dispatcher: CoroutineContext) : FbRepo<Message>("messages", db, dispatcher),
    MessageRepo {
    override suspend fun listByComplaintId(complaintId: UUID): List<Message> {
        return withCollection { coll ->
            coll.whereEqualTo("complaintId", complaintId.toString())
                .get().get()
                .map { it.data.toEntity(it.id) }
        }
    }

    override fun Message.toDoc(): Map<String, Any?> {
        return linkedMapOf(
            "id" to this.id.toString(),
            "complaintId" to this.complaintId?.toString(),
            "content" to this.content,
            "createdAt" to this.createdAt.toString(),
            "updatedAt" to this.updatedAt.toString(),
        )
    }

    override fun Map<String, Any?>.toEntity(id: String): Message {
        return Message(
            UUID.fromString(id),
            this["complaintId"]?.toString()?.let(UUID::fromString),
            this.getValue("content").toString(),
            Instant.parse(this.getValue("createdAt").toString()),
            Instant.parse(this.getValue("updatedAt").toString())
        )
    }
}
