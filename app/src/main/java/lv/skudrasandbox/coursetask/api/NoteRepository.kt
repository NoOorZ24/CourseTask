package lv.skudrasandbox.coursetask.api

import androidx.lifecycle.liveData
import kotlinx.coroutines.Dispatchers
import lv.skudrasandbox.coursetask.items.NoteData

sealed class Resource<T> {
    class Success<T>(val data: T) : Resource<T>()
    class Loading<T> : Resource<T>()
    class Loaded<T> : Resource<T>()
    class Error<T>(val message: String?) : Resource<T>()
}

object NoteRepository {

    private val service by lazy { NoteServiceProvider.instance }

    fun getNotes() = request { service.getNotes() }

    fun createNote(note: NoteData) = request { service.saveNote(note) }

    fun removeNote(id: String) = request { service.removeNote(id) }

    fun getNote(id: String) = request { service.getNote(id) }

    fun updateNote(id: String, note: NoteData) = request { service.updateNote(id, note) }

    private fun <T> request(request: suspend () -> T) = liveData<Resource<T>>(Dispatchers.IO) {
        emit(Resource.Loading())
        try {
            emit(Resource.Success(request()))
        } catch (e: Exception) {
            emit(Resource.Error(e.message))
        } finally {
            emit(Resource.Loaded())
        }
    }
}