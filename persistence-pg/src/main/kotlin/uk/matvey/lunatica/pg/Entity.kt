package uk.matvey.lunatica.pg

interface Entity<ID: ColumnValue> {
    fun id(): ID

    fun updatedAt(): ColumnValue.TimeStamp
}
