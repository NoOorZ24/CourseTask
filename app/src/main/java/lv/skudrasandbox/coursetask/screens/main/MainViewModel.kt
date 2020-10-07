package lv.skudrasandbox.coursetask.screens.main

import androidx.lifecycle.ViewModel
import lv.skudrasandbox.coursetask.api.NoteRepository
import lv.skudrasandbox.coursetask.items.NoteData

class MainViewModel(private val repository: NoteRepository = NoteRepository) : ViewModel() {

    fun getNotes() = repository.getNotes()

    fun removeNotes(id: String) = repository.removeNote(id)

}