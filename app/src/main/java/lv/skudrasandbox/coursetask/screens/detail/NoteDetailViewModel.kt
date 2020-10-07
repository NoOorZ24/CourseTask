package lv.skudrasandbox.coursetask.screens.detail

import androidx.lifecycle.ViewModel
import lv.skudrasandbox.coursetask.api.NoteRepository
import lv.skudrasandbox.coursetask.items.NoteData

class NoteDetailViewModel(private val repository: NoteRepository = NoteRepository) :
    ViewModel() {

    fun getNote(id: String) = repository.getNote(id)

    fun createNote(note: NoteData) = repository.createNote(note)

    fun updateNote(id: String, item: NoteData) = repository.updateNote(id, item)

}