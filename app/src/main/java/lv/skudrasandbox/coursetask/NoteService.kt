package lv.skudrasandbox.coursetask

import retrofit2.Call
import retrofit2.http.GET

interface NoteService {
    @GET("notes")
    fun getNote(): Call<List<NoteData>>
}