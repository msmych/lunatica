package uk.matvey.lunatica.complaint

import io.ktor.http.ContentType

class InMemoryFileStorage : FileStorage {

    private val files = mutableMapOf<String, Pair<ContentType, ByteArray>>()

    override fun upload(key: String, content: ByteArray, contentType: ContentType) {
        files[key] = contentType to content
    }

    override fun fetch(key: String): Pair<ContentType, ByteArray> {
        return files.getValue(key)
    }
}
