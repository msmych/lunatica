package uk.matvey.lunatica.complaints.messages

import com.google.cloud.firestore.Firestore
import kotlinx.coroutines.withContext
import uk.matvey.lunatica.fb.FbRepo
import java.time.Instant
import java.util.UUID
import kotlin.coroutines.CoroutineContext

class MessageFbRepo(db: Firestore, dispatcher: CoroutineContext) : FbRepo<Message>("messages", db, dispatcher),
    MessageRepo {
    override suspend fun listByComplaintId(complaintId: UUID): List<Message> {
        return withContext(dispatcher) {
            db.collection(collectionName).listDocuments().mapNotNull { it.get().get().data?.toEntity() }
        }
    }

    override fun Message.toDoc(): Map<String, Any?> {
        return linkedMapOf(
            "id" to this.id.toString(),
            "complaintId" to this.complaintId.toString(),
            "content" to this.content,
            "createdAt" to this.createdAt.toString(),
            "updatedAt" to this.updatedAt.toString(),
        )
    }

    override fun Map<String, Any?>.toEntity(): Message {
        return Message(
            UUID.fromString(this.getValue("id").toString()),
            this["complaintId"]?.toString()?.let(UUID::fromString),
            this.getValue("content").toString(),
            Instant.parse(this.getValue("createdAt").toString()),
            Instant.parse(this.getValue("updatedAt").toString())
        )
    }
}
