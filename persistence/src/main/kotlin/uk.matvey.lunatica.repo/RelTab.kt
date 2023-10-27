package uk.matvey.lunatica.repo

class RelTab(
    val columns: LinkedHashMap<String, RelCol>
) {
    fun columnNames() = columns.keys
}
