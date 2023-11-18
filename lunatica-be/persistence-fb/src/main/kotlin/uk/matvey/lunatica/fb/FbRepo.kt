package uk.matvey.lunatica.fb

import com.google.cloud.firestore.CollectionReference
import com.google.cloud.firestore.Firestore
import kotlinx.coroutines.withContext
import uk.matvey.lunatica.repo.EntityRepo
import java.time.Instant
import kotlin.coroutines.CoroutineContext

abstract class FbRepo<E>(
    val collectionName: String,
    val db: Firestore,
    val dispatcher: CoroutineContext
): EntityRepo<E> {

    override suspend fun insert(entity: E) {
        val doc = entity.toDoc()
        withCollection { coll ->
            coll.document(doc.getValue("id").toString())
                .set(doc.filterKeys { it != "id" })
        }
    }

    override suspend fun update(entity: E): Instant? {
        val doc = entity.toDoc()
        val updatedAt = Instant.now()
        return withCollection { coll ->
            coll.document(doc.getValue("id").toString())
                .set(doc.filterKeys { it != "id" } + mapOf("updated_at" to updatedAt.toString()))
            updatedAt
        }
    }

    override suspend fun delete(entity: E) {
        val doc = entity.toDoc()
        return withCollection { coll ->
            coll.document(doc.getValue("id").toString())
                .delete()
        }
    }

    abstract fun E.toDoc(): Map<String, Any?>

    abstract fun Map<String, Any?>.toEntity(id: String): E

    suspend fun <R> withCollection(block: (CollectionReference) -> R): R {
        return withContext(dispatcher) {
            block(db.collection(collectionName))
        }
    }
}
