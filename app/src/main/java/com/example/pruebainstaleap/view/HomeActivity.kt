package com.example.pruebainstaleap.view

import android.animation.ObjectAnimator
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import com.example.pruebainstaleap.R
import com.example.pruebainstaleap.db.model.ResultService
import com.example.pruebainstaleap.utils.BASE_URL_IMAGE
import com.example.pruebainstaleap.view.fragment.HomeFragment
import com.example.pruebainstaleap.view.fragment.`interface`.MoviesTvShowInterface
import com.example.pruebainstaleap.viewmodel.HomeActivityViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.toolbar.*
import kotlinx.android.synthetic.main.view_bottom_sheet_details.*
import java.util.*


class HomeActivity : AppCompatActivity(), View.OnClickListener, MoviesTvShowInterface {

    private lateinit var mainActivityViewModel: HomeActivityViewModel

    private var sheetBehavior: BottomSheetBehavior<View>? = null

    private var temporalId: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_IMMERSIVE)

        mainActivityViewModel = ViewModelProvider(this).get(HomeActivityViewModel::class.java)
        mainActivityViewModel.getMoviesNowPlaying(this, 1).observe(this, { moviesNowPlaying ->
            consumeMoviesPopular(moviesNowPlaying.results)
        })

        sheetBehavior = BottomSheetBehavior.from(clDetailMovieTvShowPoster)
        sheetBehavior?.state = BottomSheetBehavior.STATE_HIDDEN
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btPlay -> Toast.makeText(this, "Reproducir", Toast.LENGTH_LONG).show()
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
            R.id.ivCloseDetailPoster -> sheetBehavior?.state = BottomSheetBehavior.STATE_HIDDEN
        }
    }

    override fun clickMovieTvShow(resultService: ResultService?) {
        if (sheetBehavior?.state == BottomSheetBehavior.STATE_HIDDEN) {
            temporalId = resultService?.id
            setDataResultService(resultService)
            sheetBehavior?.state = BottomSheetBehavior.STATE_EXPANDED
        } else if (sheetBehavior?.state == BottomSheetBehavior.STATE_EXPANDED && temporalId == resultService?.id) {
            sheetBehavior?.state = BottomSheetBehavior.STATE_HIDDEN
        } else if (sheetBehavior?.state == BottomSheetBehavior.STATE_EXPANDED && temporalId != resultService?.id) {
            temporalId = resultService?.id
            sheetBehavior?.state = BottomSheetBehavior.STATE_HIDDEN
            setDataResultService(resultService)
            Timer().schedule(object : TimerTask() {
                override fun run() {
                    sheetBehavior?.state = BottomSheetBehavior.STATE_EXPANDED
                }
            }, 500)
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

    private fun consumeMoviesPopular(moviesNowPlaying: ArrayList<ResultService>?) {
        mainActivityViewModel.getMoviesPopular(this, 1).observe(this, { moviesPopular ->
            consumeTvAiringToday(moviesNowPlaying, moviesPopular.results)
        })
    }

    private fun consumeTvAiringToday(
        moviesNowPlaying: ArrayList<ResultService>?,
        moviesPopular: ArrayList<ResultService>?
    ) {
        mainActivityViewModel.getTvAiringToday(this, 1).observe(this, { tvAiringToday ->
            consumeTvPopular(moviesNowPlaying, moviesPopular, tvAiringToday.results)
        })
    }

    private fun consumeTvPopular(
        moviesNowPlaying: ArrayList<ResultService>?,
        moviesPopular: ArrayList<ResultService>?,
        tvAiringToday: ArrayList<ResultService>?
    ) {
        mainActivityViewModel.getTvPopular(this, 1).observe(this, { tvPopular ->
            val ft: FragmentTransaction = supportFragmentManager.beginTransaction()
            ft.replace(
                R.id.fg1,
                HomeFragment(
                    moviesNowPlaying,
                    moviesPopular,
                    tvAiringToday,
                    tvPopular.results,
                    this
                )
            )
            ft.commit()
        })
    }

    private fun setDataResultService(resultService: ResultService?) {
        ivImageDetailPoster.setImageURI(Uri.parse(BASE_URL_IMAGE + resultService?.poster_path))
        tvTitleDetailsPoster.text = resultService?.title
        tvDescriptionPoster.text = resultService?.overview
    }
}