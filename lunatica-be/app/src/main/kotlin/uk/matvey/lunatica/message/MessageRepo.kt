package uk.matvey.lunatica.message

import com.google.cloud.firestore.Firestore
import org.springframework.stereotype.Component
import java.time.Instant
import java.util.UUID

@Component
class MessageRepo(private val db: Firestore) {

    fun add(message: Message) {
        db.collection("messages").document(message.id.toString()).set(message.toDoc())
    }

    fun get(id: UUID): Message {
        val doc = db.collection("messages").document(id.toString()).get().get()
        return fromDoc(id, requireNotNull(doc.data))
    }

    private fun Message.toDoc(): Map<String, Any> {
        return listOfNotNull(
            complaintId?.let { "complaintId" to it.toString() },
            "content" to this.content,
            "createdAt" to this.createdAt.toString(),
            "updatedAt" to this.updatedAt.toString(),
        )
            .toMap()
    }

    private fun fromDoc(id: UUID, doc: Map<String, Any>): Message {
        return Message(
            id,
            doc["complaintId"]?.let { UUID.fromString(it.toString()) },
            doc.getValue("content").toString(),
            Instant.parse(doc.getValue("createdAt").toString()),
            Instant.parse(doc.getValue("updatedAt").toString()),
        )
    }
}
