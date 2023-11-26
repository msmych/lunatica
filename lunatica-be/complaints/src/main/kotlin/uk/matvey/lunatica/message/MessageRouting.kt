package uk.matvey.lunatica.message

import com.auth0.jwt.JWT
import com.pengrad.telegrambot.TelegramBot
import com.pengrad.telegrambot.request.SendMessage
import io.ktor.http.ContentType
import io.ktor.http.ContentType.Application
import io.ktor.http.HttpStatusCode.Companion.Created
import io.ktor.http.HttpStatusCode.Companion.Unauthorized
import io.ktor.http.content.PartData
import io.ktor.http.content.forEachPart
import io.ktor.http.content.streamProvider
import io.ktor.server.application.call
import io.ktor.server.request.receiveMultipart
import io.ktor.server.response.respond
import io.ktor.server.response.respondBytes
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import io.ktor.server.util.getOrFail
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.add
import kotlinx.serialization.json.buildJsonArray
import uk.matvey.lunatica.account.AccountRepo
import uk.matvey.lunatica.complaint.ComplaintRepo
import uk.matvey.lunatica.complaint.FileStorage
import java.util.UUID

fun Route.messageRouting(
    messageService: MessageService,
    messageRepo: MessageRepo,
    accountRepo: AccountRepo,
    complaintRepo: ComplaintRepo,
    fileStorage: FileStorage,
    bot: TelegramBot?
) {
    route("/messages") {
        get {
            val complaintId = UUID.fromString(call.request.queryParameters.getOrFail("complaintId"))
            val messages = messageRepo.listByComplaintId(complaintId)
            call.respond(messages)
        }
        post { request: CreateMessageRequest ->
            val token = call.request.cookies["auth"] ?: return@post call.respond(Unauthorized)
            val accountId = UUID.fromString(JWT.decode(token).subject)
            val message = messageService.createMessage(accountId, request.complaintId, request.content)
            bot?.let {
                message.complaintId?.let { complaintId ->
                    val complaint = complaintRepo.get(complaintId)
                    if (complaint.accountId != accountId) {
                        val receiverAccount = accountRepo.get(complaint.accountId)
                        receiverAccount.tgChatId?.let { tgChatId ->
                            bot.execute(SendMessage(tgChatId, "Обновление по вашему обращению:\n\n${message.content}"))
                        }
                    }
                }
            }
            call.respond(Created, """{"id":"${message.id}"}""")
        }
        route("/attachments") {
            get("/{key}") {
                val attachmentKey = call.parameters.getOrFail("key")
                val attachment = fileStorage.fetch(attachmentKey)
                call.respondBytes(attachment.second, attachment.first)
            }
            post {
                val token = call.request.cookies["auth"] ?: return@post call.respond(Unauthorized)
                val accountId = UUID.fromString(JWT.decode(token).subject)
                val multipart = call.receiveMultipart()
                var complaintId: UUID? = null
                val files = mutableMapOf<String, Pair<ByteArray, ContentType>>()
                multipart.forEachPart { part ->
                    when (part) {
                        is PartData.FormItem -> {
                            if (part.name == "complaintId") {
                                complaintId = UUID.fromString(part.value)
                            }
                        }

                        is PartData.FileItem -> {
                            files[part.originalFileName ?: "attachment"] =
                                part.streamProvider().readAllBytes() to (part.contentType ?: Application.OctetStream)
                        }

                        else -> {}
                    }
                }
                val messagesIds = complaintId?.let {
                    files.map { (name, content) ->
                        val message = Message.complaintAttachment(accountId, it, name)
                        messageRepo.insert(message)
                        fileStorage.upload(requireNotNull(message.attachmentKey), content.first, content.second)
                        message.id
                    }
                } ?: listOf()
                call.respond(Created, buildJsonArray { messagesIds.forEach { id -> add(id.toString()) } })
            }
        }
    }
}

@Serializable
data class CreateMessageRequest(
    val complaintId: @Contextual UUID,
    val content: String,
)
