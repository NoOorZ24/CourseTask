package lv.skudrasandbox.coursetask.api

import lv.skudrasandbox.coursetask.items.ItemId
import lv.skudrasandbox.coursetask.items.NoteData
import retrofit2.http.*

interface NoteService {

    @GET("notes")
    suspend fun getNotes(): List<NoteData>

    @GET("notes/{id}")
    suspend fun getNote(@Path("id") id: String): NoteData

    @POST("notes")
    suspend fun saveNote(@Body item: NoteData)

    @DELETE("notes/{id}")
    suspend fun removeNote(@Path("id") id: String)

    @PUT("notes/{id}")
    suspend fun updateNote(
        @Path("id") id: String,
        @Body item: NoteData
    )

}