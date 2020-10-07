package lv.skudrasandbox.coursetask.items

data class NoteData(
    val id: String,
    val title: String,
    val type: String,
    val json: String,
    val color: String,
    val is_important: Boolean
)

data class ItemId(val id: String)