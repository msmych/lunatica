package uk.matvey.lunatica.fb

import com.google.cloud.firestore.Firestore
import kotlinx.coroutines.withContext
import uk.matvey.lunatica.repo.EntityRepo
import java.time.Instant
import kotlin.coroutines.CoroutineContext

abstract class FbRepo<E>(val collectionName: String, val db: Firestore, val dispatcher: CoroutineContext): EntityRepo<E> {

    override suspend fun insert(entity: E) {
        withContext(dispatcher) {
            val doc = entity.toDoc()
            db.collection(collectionName).document(doc.getValue("id").toString()).set(doc)
        }
    }

    override suspend fun update(entity: E): Instant? {
        return withContext(dispatcher) {
            val doc = entity.toDoc()
            val updatedAt = Instant.now()
            db.collection(collectionName).document(doc.getValue("id").toString())
                .set(doc + mapOf("updatedAt" to updatedAt.toString()))
            updatedAt
        }
    }

    abstract fun E.toDoc(): Map<String, Any?>

    abstract fun Map<String, Any?>.toEntity(): E
}
