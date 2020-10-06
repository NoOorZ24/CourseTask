package lv.skudrasandbox.coursetask

data class NoteData(
    val id: Long,
    val type: String,
    val json: String,
    val color: String,
    val is_archived: Boolean,
    val url: String
)

sealed class KeepItem

data class KeepItemText(val title: String, val text: String) : KeepItem()

data class KeepItemImage(val uri: String) : KeepItem()

data class KeepItemRadio(val a: String, val b: String, val c: String) : KeepItem()