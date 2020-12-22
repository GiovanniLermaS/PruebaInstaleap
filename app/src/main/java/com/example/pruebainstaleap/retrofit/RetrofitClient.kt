package com.example.pruebainstaleap.retrofit

import android.content.Context
import com.example.pruebainstaleap.utils.BASE_URL
import com.example.pruebainstaleap.utils.hasNetwork
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitClient(context: Context) {

    private val cacheSize = (5 * 1024 * 1024).toLong()
    private val myCache = Cache(context.cacheDir, cacheSize)

    private val okHttpClient = OkHttpClient.Builder()
        .cache(myCache)
        .addInterceptor { chain ->
            var request = chain.request()
            request = if (hasNetwork(context)!!)
                request.newBuilder().header("Cache-Control", "public, max-age=" + 5).build()
            else
                request.newBuilder().header(
                    "Cache-Control",
                    "public, only-if-cached, max-stale=" + 60 * 60 * 24 * 7
                ).build()
            chain.proceed(request)
        }.build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttpClient)
        .build()

    val apiInterface: ApiInterface = retrofit
        .create(ApiInterface::class.java)
}