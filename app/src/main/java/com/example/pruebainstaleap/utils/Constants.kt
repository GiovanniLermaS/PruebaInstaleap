package com.example.pruebainstaleap.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo

const val BASE_URL = "https://api.themoviedb.org/3/"
const val BASE_URL_IMAGE = "https://image.tmdb.org/t/p/original"
const val API_KEY = "a85162e941d8786f851eae457b3f6761"
const val LANGUAGE = "es-ES"
const val MOVIES_NOW_PLAYING = "movie/now_playing?api_key=$API_KEY&language=$LANGUAGE"
const val MOVIES_POPULAR = "movie/popular?api_key=$API_KEY&language=$LANGUAGE"
const val TV_AIRING_TODAY = "tv/airing_today?api_key=$API_KEY&language=$LANGUAGE\""
const val TV_POPULAR = "tv/popular?api_key=$API_KEY&language=$LANGUAGE\""
const val RESULT_SERVICE = "resultService"

fun hasNetwork(context: Context): Boolean? {
    var isConnected: Boolean? = false
    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo
    if (activeNetwork != null && activeNetwork.isConnected)
        isConnected = true
    return isConnected
}