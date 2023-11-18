package uk.matvey.lunatica.repo

import java.time.Instant

interface EntityRepo<E> {
    suspend fun insert(entity: E)

    suspend fun update(entity: E): Instant?

    suspend fun delete(entity: E)
}
