package uk.matvey.lunatica.message

import java.util.UUID

class MessageService(private val messageRepo: MessageRepo) {

    suspend fun createMessage(authorId: UUID, complaintId: UUID, content: String): Message {
        val message = Message.complaintMessage(authorId, complaintId, content)
        messageRepo.insert(message)
        return message
    }
}
