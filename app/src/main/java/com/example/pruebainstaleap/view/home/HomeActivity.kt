package com.example.pruebainstaleap.view.home

import android.animation.ObjectAnimator
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.pruebainstaleap.R
import com.example.pruebainstaleap.db.model.ResultService
import com.example.pruebainstaleap.utils.BASE_URL_IMAGE
import com.example.pruebainstaleap.utils.RESULT_SERVICE
import com.example.pruebainstaleap.view.detail.DetailActivity
import com.example.pruebainstaleap.view.home.fragment.MoviesTvShowFragment
import com.example.pruebainstaleap.view.home.fragment.interfaces.MoviesTvShowInterface
import com.example.pruebainstaleap.viewmodel.HomeActivityViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.toolbar.*
import kotlinx.android.synthetic.main.view_bottom_sheet_details.*
import java.util.*

class HomeActivity : AppCompatActivity(), View.OnClickListener, MoviesTvShowInterface {

    private lateinit var mainActivityViewModel: HomeActivityViewModel

    private var sheetBehavior: BottomSheetBehavior<View>? = null

    private var temporalId: Int? = null

    private var resultService: ResultService? = null

    private var moviesNowPlaying: ArrayList<ResultService>? = null

    private var moviesPopular: ArrayList<ResultService>? = null

    private var tvAiringToday: ArrayList<ResultService>? = null

    private var tvPopular: ArrayList<ResultService>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_IMMERSIVE)

        mainActivityViewModel = ViewModelProvider(this).get(HomeActivityViewModel::class.java)
        mainActivityViewModel.getMoviesNowPlaying(this, 1)
            .observe(this, Observer { moviesNowPlaying ->
                consumeMoviesPopular(moviesNowPlaying.results)
            })
        sheetBehavior = BottomSheetBehavior.from(clDetailBottom)
        sheetBehavior?.state = BottomSheetBehavior.STATE_HIDDEN
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btPlay -> Toast.makeText(this, "Reproducir", Toast.LENGTH_LONG).show()
            R.id.tvShows -> showByType(tvShows, tvMovies, false)
            R.id.tvMovies -> showByType(tvMovies, tvShows, true)
            R.id.ivCloseBottom -> sheetBehavior?.state = BottomSheetBehavior.STATE_HIDDEN
            R.id.clDetailBottom -> {
                val intent = Intent(this, DetailActivity::class.java)
                intent.putExtra(RESULT_SERVICE, resultService)
                startActivity(intent)
            }
        }
    }

    override fun clickMovieTvShow(resultService: ResultService?) {
        this.resultService = resultService
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
        this.moviesNowPlaying = moviesNowPlaying
        this.moviesPopular = moviesPopular
        this.tvAiringToday = tvAiringToday
        mainActivityViewModel.getTvPopular(this, 1).observe(this, { tvPopular ->
            this.tvPopular = tvPopular.results
            showFragment(
                moviesNowPlaying!![0],
                moviesNowPlaying,
                moviesPopular,
                tvAiringToday,
                tvPopular.results
            )
        })
    }

    private fun setDataResultService(resultService: ResultService?) {
        ivImageBottom.setImageURI(Uri.parse(BASE_URL_IMAGE + resultService?.poster_path))
        tvTitleBottom.text = resultService?.title
        tvDescriptionBottom.text = resultService?.overview
    }

    private fun showByType(
        tvAnimation: TextView?,
        tvShowHide1: TextView?,
        isMovie: Boolean?
    ) {
        if (tvShowHide1?.visibility == View.VISIBLE) {
            tvShowHide1.visibility = View.INVISIBLE
            animationTextView(tvAnimation!!, -tvAnimation.x + 250f)
            if (isMovie!!)
                showFragment(moviesNowPlaying!![0], moviesNowPlaying, moviesPopular, null, null)
            else showFragment(tvAiringToday!![0], null, null, tvAiringToday, tvPopular)
        } else {
            animationTextView(tvAnimation!!, -tvAnimation.x + 250f)
            Timer().schedule(object : TimerTask() {
                override fun run() {
                    tvShowHide1?.visibility = View.VISIBLE
                }
            }, 1000)
            showFragment(
                moviesNowPlaying!![0],
                moviesNowPlaying,
                moviesPopular,
                tvAiringToday,
                tvPopular
            )
        }
    }

    private fun showFragment(
        resultService: ResultService?,
        listMoviesNowPlaying: ArrayList<ResultService>?,
        listMoviesPopular: ArrayList<ResultService>?,
        listTvAiringToday: ArrayList<ResultService>?,
        listTvPopular: ArrayList<ResultService>?
    ) {
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(
            R.id.fg1,
            MoviesTvShowFragment(
                resultService,
                listMoviesNowPlaying,
                listMoviesPopular,
                listTvAiringToday,
                listTvPopular,
                this
            )
        )
        ft.commit()
    }

    override fun onBackPressed() {
        when {
            tvShows.visibility == View.VISIBLE -> showByType(tvShows, tvMovies, false)
            tvMovies.visibility == View.VISIBLE -> showByType(tvMovies, tvShows, false)
            else -> super.onBackPressed()
        }
    }
}