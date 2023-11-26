package uk.matvey.lunatica.pg

import uk.matvey.lunatica.pg.RelCol.TimeStamp

interface Entity<ID: RelCol> {
    fun idRel(): ID

    fun updatedAtRel(): TimeStamp
}
