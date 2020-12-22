package com.example.pruebainstaleap.repository

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.pruebainstaleap.db.model.ResponseService
import com.example.pruebainstaleap.retrofit.RetrofitClient
import retrofit2.Call
import retrofit2.Callback

class HomeActivityRepository {

    private val serviceLatestMovie =
        MutableLiveData<ResponseService>()

    fun getResponse(
        context: Context,
        url: String,
        page: Int
    ): MutableLiveData<ResponseService> {
        val call = RetrofitClient(context).apiInterface.getNowPlayingMovies(url, page)
        call.enqueue(object : Callback<ResponseService> {
            override fun onFailure(
                call: Call<ResponseService>,
                t: Throwable
            ) {
                Log.v("DEBUG : ", t.message.toString())
            }

            override fun onResponse(
                call: Call<ResponseService>,
                responseService: retrofit2.Response<ResponseService>
            ) {
                serviceLatestMovie.value =
                    responseService.body() as ResponseService
            }
        })

        return serviceLatestMovie
    }
}