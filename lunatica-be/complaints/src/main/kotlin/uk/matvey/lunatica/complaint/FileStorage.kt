package uk.matvey.lunatica.complaint

import io.ktor.http.ContentType

interface FileStorage {

    fun upload(key: String, content: ByteArray, contentType: ContentType)

    fun fetch(key: String): Pair<ContentType, ByteArray>
}
