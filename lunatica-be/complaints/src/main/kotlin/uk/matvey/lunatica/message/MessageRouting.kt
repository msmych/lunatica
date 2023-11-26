package uk.matvey.lunatica.message

import com.amazonaws.services.s3.AmazonS3ClientBuilder
import com.auth0.jwt.JWT
import io.ktor.http.ContentType.Application.OctetStream
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
import java.util.UUID

fun Route.messageRouting(messageService: MessageService, messageRepo: MessageRepo) {
    route("/messages") {
        get {
            val complaintId = UUID.fromString(call.request.queryParameters.getOrFail("complaintId"))
            val messages = messageRepo.listByComplaintId(complaintId)
            call.respond(Created, messages)
        }
        post { request: CreateMessageRequest ->
            val token = call.request.cookies["auth"] ?: return@post call.respond(Unauthorized)
            val accountId = UUID.fromString(JWT.decode(token).subject)
            val message = messageService.createMessage(accountId, request.complaintId, request.content)
            call.respond(Created, """{"id":"${message.id}"}""")
        }
        get("/attachments/{key}") {
            val attachmentKey = call.parameters.getOrFail("key")
            val s3Client = AmazonS3ClientBuilder.standard().build()
            val attachment = s3Client.getObject("lunatica-attachments", attachmentKey).objectContent.readAllBytes()
            call.respondBytes(attachment, OctetStream)
        }
        post("/attachments") {
            val token = call.request.cookies["auth"] ?: return@post call.respond(Unauthorized)
            val accountId = UUID.fromString(JWT.decode(token).subject)
            val multipart = call.receiveMultipart()
            var complaintId: UUID? = null
            val files = mutableMapOf<String, ByteArray>()
            multipart.forEachPart { part ->
                when (part) {
                    is PartData.FormItem -> {
                        if (part.name == "complaintId") {
                            complaintId = UUID.fromString(part.value)
                        }
                    }
                    is PartData.FileItem -> {
                        files[part.originalFileName ?: "attachment"] = part.streamProvider().readAllBytes()
                    }
                    else -> {}
                }
            }
            complaintId?.let {
                files.forEach { (name, content) ->
                    val message = Message.complaintAttachment(accountId, it, name)
                    val s3client = AmazonS3ClientBuilder.standard().build()
                    s3client.putObject("lunatica-attachments", message.attachmentKey, String(content))
                }
            }
        }
    }
}

@Serializable
data class CreateMessageRequest(
    val complaintId: @Contextual UUID,
    val content: String,
)
