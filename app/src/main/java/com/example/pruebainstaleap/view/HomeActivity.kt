package com.example.pruebainstaleap.view

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import com.example.pruebainstaleap.R
import com.example.pruebainstaleap.utils.BASE_URL_IMAGE
import com.example.pruebainstaleap.view.fragment.HomeFragment
import com.example.pruebainstaleap.viewmodel.HomeActivityViewModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.appbar.*
import kotlinx.android.synthetic.main.principal_image_home.*


class HomeActivity : AppCompatActivity(), View.OnClickListener {

    lateinit var mainActivityViewModel: HomeActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )

        val ft: FragmentTransaction = supportFragmentManager.beginTransaction()
        ft.replace(R.id.fg1, HomeFragment())
        ft.commit()

        mainActivityViewModel = ViewModelProvider(this).get(HomeActivityViewModel::class.java)
        mainActivityViewModel.getMoviesNowPlaying(this, 1).observe(this, { moviesNowPlaying ->
            Picasso.get().load(BASE_URL_IMAGE + moviesNowPlaying.results!![0].backdrop_path)
                .into(ivPrincipalImage)
        })
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tvShows -> {
                animationTextView(tvShows, -tvShows.x + 250f)
                tvMovies.visibility = View.INVISIBLE
                tvMyList.visibility = View.INVISIBLE
            }
            R.id.tvMovies -> {
                tvShows.visibility = View.INVISIBLE
                animationTextView(tvMovies, -tvMovies.x + 250)
                tvMyList.visibility = View.INVISIBLE
            }
            R.id.tvMyList -> {
                tvShows.visibility = View.INVISIBLE
                tvMovies.visibility = View.INVISIBLE
                animationTextView(tvMyList, -tvMyList.x + 250)
            }
        }
    }

    private fun animationTextView(
        view: View,
        translateX: Float
    ) {
        ObjectAnimator.ofFloat(view, "translationX", translateX).apply {
            duration = 700
            start()
        }
    }

    private fun consumeMoviesPopular() {
        mainActivityViewModel.getMoviesPopular(this, 1).observe(this, { moviesPopular ->
            moviesPopular.results
        })
    }
}