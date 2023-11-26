package uk.matvey.lunatica.complaint

import com.amazonaws.services.s3.AmazonS3
import io.ktor.http.ContentType
import io.ktor.http.ContentType.Application
import io.ktor.http.ContentType.Image

class S3FileStorage(private val s3Client: AmazonS3) : FileStorage {
    override fun upload(key: String, content: ByteArray, contentType: ContentType) {
        s3Client.putObject("lunatica-attachments", key, String(content))
    }

    override fun fetch(key: String): Pair<ContentType, ByteArray> {
        val obj = s3Client.getObject("lunatica-attachments", key)
        val contentType = when (key.substringAfterLast('.')) {
            "pdf" -> Application.Pdf
            "jpeg", "jpg" -> Image.JPEG
            "png" -> Image.PNG
            else -> Application.OctetStream
        }
        return contentType to obj.objectContent.readAllBytes()
    }
}
