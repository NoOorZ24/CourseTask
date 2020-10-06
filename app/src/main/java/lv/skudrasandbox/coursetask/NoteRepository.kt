package lv.skudrasandbox.coursetask

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class NoteRepository {
    private val retrofit = Retrofit.Builder()
        .baseUrl("http://10.0.2.2:8000/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val service = retrofit.create(NoteService::class.java)

    fun getNote(): LiveData<NoteData> {
        val data = MutableLiveData<NoteData>()

        service.getNote().enqueue(object : Callback<List<NoteData>> {
            override fun onResponse(
                call: Call<List<NoteData>>,
                response: Response<List<NoteData>>
            ) {
                println(response.body())
                data.postValue(response.body()?.first())
            }

            override fun onFailure(call: Call<List<NoteData>>, t: Throwable) {
                println(t.message)
                //I'm feeling lucky!
            }
        })

        return data
    }
}