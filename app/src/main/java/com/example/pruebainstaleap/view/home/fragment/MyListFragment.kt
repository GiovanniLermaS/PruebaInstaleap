package com.example.pruebainstaleap.view.home.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.example.pruebainstaleap.R
import com.example.pruebainstaleap.db.model.ResultService
import com.example.pruebainstaleap.utils.database
import com.example.pruebainstaleap.view.home.fragment.adapter.MoviesTvShowAdapter
import com.example.pruebainstaleap.view.home.fragment.interfaces.MoviesTvShowInterface
import kotlinx.coroutines.launch

class MyListFragment(
    private val moviesTvShowInterface: MoviesTvShowInterface?
) : Fragment() {

    private var listResultService: List<ResultService>? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.fragment_my_list, container, false)
        lifecycleScope.launch {
            listResultService = database?.resultServiceDao()?.getResultService()
            if (listResultService != null && listResultService!!.isNotEmpty()) {
                v.findViewById<RecyclerView>(R.id.tvMyList).adapter = MoviesTvShowAdapter(
                    listResultService as ArrayList<ResultService>,
                    moviesTvShowInterface
                )
            }
        }
        return v
    }
}