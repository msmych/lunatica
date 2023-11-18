package uk.matvey.lunatica.repo

import uk.matvey.lunatica.repo.RelCol.TimeStamp

interface Entity<ID: RelCol> {
    fun id(): ID

    fun updatedAt(): TimeStamp
}
