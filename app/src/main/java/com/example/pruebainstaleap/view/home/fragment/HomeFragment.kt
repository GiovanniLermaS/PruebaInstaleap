package com.example.pruebainstaleap.view.home.fragment

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.pruebainstaleap.R
import com.example.pruebainstaleap.db.model.ResultService
import com.example.pruebainstaleap.utils.BASE_URL_IMAGE
import com.example.pruebainstaleap.view.home.fragment.`interface`.MoviesTvShowInterface
import com.example.pruebainstaleap.view.home.fragment.adapter.MoviesTvShowAdapter
import com.facebook.drawee.view.SimpleDraweeView

class HomeFragment(
    private val listMoviesNowPlaying: ArrayList<ResultService>?,
    private val listMoviesPopular: ArrayList<ResultService>?,
    private val listTvAiringToday: ArrayList<ResultService>?,
    private val listTvPopular: ArrayList<ResultService>?,
    private val moviesTvShowInterface: MoviesTvShowInterface?
) : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.fragment_home, container, false)

        v.findViewById<ConstraintLayout>(R.id.clInfoPoster)
            .setOnClickListener { moviesTvShowInterface?.clickMovieTvShow(listMoviesNowPlaying!![0]) }

        v.findViewById<SimpleDraweeView>(R.id.ivPrincipalImage)
            .setImageURI(Uri.parse(BASE_URL_IMAGE + listMoviesNowPlaying!![0].backdrop_path))
        v.findViewById<TextView>(R.id.tvNameMovie).text = listMoviesNowPlaying[0].title

        v.findViewById<TextView>(R.id.tvOne).text = getString(R.string.moviesNowPlaying)
        v.findViewById<RecyclerView>(R.id.rvOne).adapter =
            MoviesTvShowAdapter(listMoviesNowPlaying, moviesTvShowInterface)

        v.findViewById<TextView>(R.id.tvTwo).text = getString(R.string.moviesPopular)
        v.findViewById<RecyclerView>(R.id.rvTwo).adapter =
            MoviesTvShowAdapter(listMoviesPopular, moviesTvShowInterface)

        v.findViewById<TextView>(R.id.tvThree).text = getString(R.string.tvAiringToday)
        v.findViewById<RecyclerView>(R.id.rvThree).adapter =
            MoviesTvShowAdapter(listTvAiringToday, moviesTvShowInterface)

        v.findViewById<TextView>(R.id.tvFour).text = getString(R.string.tvPopular)
        v.findViewById<RecyclerView>(R.id.rvFour).adapter =
            MoviesTvShowAdapter(listTvPopular, moviesTvShowInterface)
        return v
    }
}