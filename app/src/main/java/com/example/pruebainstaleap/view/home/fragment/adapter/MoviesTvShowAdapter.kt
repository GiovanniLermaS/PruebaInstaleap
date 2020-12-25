package com.example.pruebainstaleap.view.home.fragment.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.pruebainstaleap.R
import com.example.pruebainstaleap.db.model.ResultService
import com.example.pruebainstaleap.utils.BASE_URL_IMAGE
import com.example.pruebainstaleap.view.home.fragment.interfaces.MoviesTvShowInterface
import com.facebook.drawee.view.SimpleDraweeView

class MoviesTvShowAdapter(private val listMoviesTvSeasons: ArrayList<ResultService>?, private val moviesTvShowInterface: MoviesTvShowInterface?) :
    RecyclerView.Adapter<MoviesTvShowAdapter.ViewHolder>() {

    class ViewHolder(val imageView: SimpleDraweeView) : RecyclerView.ViewHolder(imageView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.view_image, parent, false) as SimpleDraweeView
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.imageView.setImageURI(Uri.parse(BASE_URL_IMAGE + listMoviesTvSeasons!![position].poster_path))
        holder.imageView.setOnClickListener { moviesTvShowInterface?.clickMovieTvShow( listMoviesTvSeasons[position]) }
    }

    override fun getItemCount() = listMoviesTvSeasons?.size!!
}