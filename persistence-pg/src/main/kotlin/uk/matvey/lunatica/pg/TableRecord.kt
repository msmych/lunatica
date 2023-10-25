package uk.matvey.lunatica.pg

class TableRecord(
    val columns: LinkedHashMap<String, ColumnValue>
) {
    fun columnNames() = columns.keys
}
