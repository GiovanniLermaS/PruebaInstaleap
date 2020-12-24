package com.example.pruebainstaleap.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.pruebainstaleap.db.model.ResponseService
import com.example.pruebainstaleap.repository.HomeActivityRepository
import com.example.pruebainstaleap.utils.MOVIES_NOW_PLAYING
import com.example.pruebainstaleap.utils.MOVIES_POPULAR
import com.example.pruebainstaleap.utils.TV_AIRING_TODAY
import com.example.pruebainstaleap.utils.TV_POPULAR

class HomeActivityViewModel : ViewModel() {

    private var servicesLiveDataMoviesNowPlaying: MutableLiveData<ResponseService>? = null
    private var servicesLiveDataMoviesPopular: MutableLiveData<ResponseService>? = null
    private var servicesLiveDataTvAiringToday: MutableLiveData<ResponseService>? = null
    private var servicesLiveDataTvPopular: MutableLiveData<ResponseService>? = null

    fun getMoviesNowPlaying(context: Context, page: Int): LiveData<ResponseService> {
        servicesLiveDataMoviesNowPlaying = HomeActivityRepository().getResponse(context, MOVIES_NOW_PLAYING, page)
        return servicesLiveDataMoviesNowPlaying as MutableLiveData<ResponseService>
    }

    fun getMoviesPopular(context: Context, page: Int): LiveData<ResponseService> {
        servicesLiveDataMoviesPopular = HomeActivityRepository().getResponse(context, MOVIES_POPULAR, page)
        return servicesLiveDataMoviesPopular as MutableLiveData<ResponseService>
    }

    fun getTvAiringToday(context: Context, page: Int): LiveData<ResponseService> {
        servicesLiveDataTvAiringToday = HomeActivityRepository().getResponse(context, TV_AIRING_TODAY, page)
        return servicesLiveDataTvAiringToday as MutableLiveData<ResponseService>
    }

    fun getTvPopular(context: Context, page: Int): LiveData<ResponseService> {
        servicesLiveDataTvPopular = HomeActivityRepository().getResponse(context, TV_POPULAR, page)
        return servicesLiveDataTvPopular as MutableLiveData<ResponseService>
    }
}