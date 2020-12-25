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
import com.example.pruebainstaleap.view.home.fragment.adapter.MoviesTvShowAdapter
import com.example.pruebainstaleap.view.home.fragment.interfaces.MoviesTvShowInterface
import com.facebook.drawee.view.SimpleDraweeView

class MoviesTvShowFragment(
    private val movieTvShow: ResultService?,
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

        //Principal image
        v.findViewById<ConstraintLayout>(R.id.clInfoPoster)
            .setOnClickListener { moviesTvShowInterface?.clickMovieTvShow(movieTvShow) }

        v.findViewById<SimpleDraweeView>(R.id.ivPrincipalImage)
            .setImageURI(Uri.parse(BASE_URL_IMAGE + movieTvShow?.backdrop_path))
        v.findViewById<TextView>(R.id.tvNameMovie).text = movieTvShow?.title
        val tvOne = v.findViewById<TextView>(R.id.tvOne)
        val rvOne = v.findViewById<RecyclerView>(R.id.rvOne)

        val tvTwo = v.findViewById<TextView>(R.id.tvTwo)
        val rvTwo = v.findViewById<RecyclerView>(R.id.rvTwo)

        val tvThree = v.findViewById<TextView>(R.id.tvThree)
        val rvThree = v.findViewById<RecyclerView>(R.id.rvThree)

        val tvFour = v.findViewById<TextView>(R.id.tvFour)
        val rvFour = v.findViewById<RecyclerView>(R.id.rvFour)

        //Movies playing
        if (listMoviesNowPlaying != null) setTexViewAndRecyclerView(
            tvOne,
            rvOne,
            getString(R.string.moviesNowPlaying),
            listMoviesNowPlaying
        )

        //Movies popular
        if (listMoviesPopular != null) setTexViewAndRecyclerView(
            tvTwo,
            rvTwo,
            getString(R.string.moviesPopular),
            listMoviesPopular
        )

        //TV on air
        if (listTvAiringToday != null) setTexViewAndRecyclerView(
            tvThree,
            rvThree,
            getString(R.string.tvAiringToday),
            listTvAiringToday
        )

        //TV popular
        if (listTvPopular != null) setTexViewAndRecyclerView(
            tvFour,
            rvFour,
            getString(R.string.tvPopular),
            listTvPopular
        )
        return v
    }

    private fun setTexViewAndRecyclerView(
        textView: TextView,
        recyclerView: RecyclerView,
        text: String,
        listResultService: ArrayList<ResultService>?
    ) {
        textView.visibility = View.VISIBLE
        recyclerView.visibility = View.VISIBLE
        textView.text = text
        recyclerView.adapter = MoviesTvShowAdapter(listResultService, moviesTvShowInterface)
    }
}