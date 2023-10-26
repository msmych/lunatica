package uk.matvey.lunatica.pg

import java.time.Instant

interface EntityRepo<E> {
    suspend fun insert(entity: E)

    suspend fun update(entity: E): Instant?
}
