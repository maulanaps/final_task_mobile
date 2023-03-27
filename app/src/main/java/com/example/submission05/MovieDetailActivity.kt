package com.example.submission03

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.submission03.databinding.ActivityMovieDetailBinding
import com.example.submission03.model.Movie
import com.example.submission05.dialog.ErrorDialog
import com.example.submission05.db.AppDatabase
import com.faltenreich.skeletonlayout.Skeleton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

private lateinit var binding: ActivityMovieDetailBinding

class MovieDetailActivity : AppCompatActivity() {
    @SuppressLint("UseCompatLoadingForDrawables", "SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMovieDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // EXTRAS
        val movieDetail = intent.getParcelableExtra<Movie>(MOVIE_DETAIL) as Movie
        val title = intent.getStringExtra(PAGE_TITLE)

        supportActionBar?.title = title

        val skeletonBackdrop: Skeleton = findViewById(R.id.skeletonLayoutBackdrop)
        skeletonBackdrop.showSkeleton()
        Glide.with(this)
            .load("https://image.tmdb.org/t/p/original" + movieDetail.backdropPath)
            .addListener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable?>?,
                    isFirstResource: Boolean
                ): Boolean {
                    Log.d("foo", "onLoadFailed: FAILED")
                    ErrorDialog.showError(this@MovieDetailActivity, "Failed to load data")
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable?>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    Log.d("foo", "onResourceReady: backdrop image ready")
                    skeletonBackdrop.showOriginal()
                    return false
                }
            })
            .into(binding.detailBackdrop)


        val skeletonPoster: Skeleton = findViewById(R.id.skeletonLayoutPoster)
        skeletonPoster.showSkeleton()
        Glide.with(this)
            .load("https://image.tmdb.org/t/p/original" + movieDetail.posterPath)
            .addListener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable?>?,
                    isFirstResource: Boolean
                ): Boolean {
                    Log.d("foo", "onLoadFailed: POSTER IMAGE FAILED")
                    ErrorDialog.showError(this@MovieDetailActivity, "Failed to load data")
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable?>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    Log.d("foo", "onResourceReady: POSTER image ready")
//                    posterSkeleton.showOriginal()
                    skeletonPoster.showOriginal()
                    return false
                }
            })
            .into(binding.detailPoster)

        binding.apply {
            detailTitle.text = movieDetail.title
            detailReleaseDate.text = movieDetail.releaseDate
            detailRating.text = "⭐ ${movieDetail.voteAverage}"
            detailOverview.text = movieDetail.overview

            val db = AppDatabase.getInstance(this@MovieDetailActivity)

            val movieDao = db.roomMovieDao()
            movieDao
                .getFavoriteById(movieDetail.id.toString())
                .observe(this@MovieDetailActivity) {
                    if (it.isEmpty()) {
                        ivAddFavorite.visibility = View.VISIBLE
                        ivRemoveFavorite.visibility = View.GONE
                    } else {
                        ivAddFavorite.visibility = View.GONE
                        ivRemoveFavorite.visibility = View.VISIBLE
                    }
                }

            ivAddFavorite.setOnClickListener {
                CoroutineScope(Dispatchers.IO).launch {
                    movieDao.insert(movieDetail)
                    Log.d("blah", "addFavorite: movie ${movieDetail.title} saved to favorite")
                }
            }

            ivRemoveFavorite.setOnClickListener {
                CoroutineScope(Dispatchers.IO).launch {
                    movieDao.delete(movieDetail)
                    Log.d("blah", "removeFavorite: movie ${movieDetail.title} removed from favorite")
                }
            }
        }

    }

    companion object {
        const val PAGE_TITLE = "page_title"
        const val MOVIE_DETAIL = "movie_detail"

        fun open(activity: AppCompatActivity, title: String?, data: Movie) {
            val intent = Intent(activity, MovieDetailActivity::class.java)
            intent.putExtra(PAGE_TITLE, title)
            intent.putExtra(MOVIE_DETAIL, data)
            ActivityCompat.startActivity(activity, intent, null)
        }
    }
}