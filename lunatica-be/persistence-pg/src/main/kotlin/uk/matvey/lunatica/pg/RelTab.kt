package uk.matvey.lunatica.pg

class RelTab(
    val columns: LinkedHashMap<String, RelCol>
) {
    fun columnNames() = columns.keys

    companion object {
        fun relTab(columns: LinkedHashMap<String, RelCol>): RelTab {
            return RelTab(columns)
        }
    }
}
