package com.example.submission05.ui.detail_movie_tvshow

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.*
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.submission05.ui.comment_form.CommentFormActivity
import com.example.submission03.R
import com.example.submission03.databinding.ActivityMovieDetailBinding
import com.example.submission03.model.MovieAndTvShow
import com.example.submission05.constant.Constants.Companion.EDIT_COMMENT
import com.example.submission05.constant.Constants.Companion.WRITE_COMMENT
import com.example.submission05.utils.dialog.ErrorDialog
import com.example.submission05.data.room.comment.CommentDatabase
import com.example.submission05.data.room.watchlist.WatchListDatabase
import com.example.submission05.data.entity.CommentEntity
import com.faltenreich.skeletonlayout.Skeleton

class MovieDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMovieDetailBinding
    private lateinit var adapter: CommentAdapter
    private lateinit var skeletonBackdrop: Skeleton
    private lateinit var skeletonPoster: Skeleton

    // VIEW MODEL
    private val viewModel: MovieDetailViewModel by viewModels {
        val movieId = intent.getStringExtra(MOVIE_ID)
        val watchListDao = WatchListDatabase.getInstance(this).WatchListDao()
        val commentDao = CommentDatabase.getInstance(this).CommentDao()
        MovieDetailViewModel.provideFactory(watchListDao, commentDao, movieId!!, this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMovieDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // EXTRAS
        val movieAndTvShowDetail =
            intent.getParcelableExtra<MovieAndTvShow>(MOVIE_DETAIL) as MovieAndTvShow
        val title = intent.getStringExtra(PAGE_TITLE)

        supportActionBar?.title = title

        // RV AND ADAPTER
        val layoutManager = LinearLayoutManager(this@MovieDetailActivity, VERTICAL, false)
        binding.rvComment.layoutManager = layoutManager
        adapter = CommentAdapter()
        binding.rvComment.adapter = adapter

        // SKELETON
        skeletonBackdrop = findViewById(R.id.skeletonLayoutBackdrop)
        skeletonPoster = findViewById(R.id.skeletonLayoutPoster)

        // GET DATA
        getData(movieAndTvShowDetail.backdropPath, movieAndTvShowDetail.posterPath)

        // SWIPE REFRESH
        binding.swipeRefreshLayout.setOnRefreshListener {
            binding.swipeRefreshLayout.isRefreshing = false
            Log.d("blah2", "onRefresh: dor")
            getData(movieAndTvShowDetail.backdropPath, movieAndTvShowDetail.posterPath)
        }

        // GET DATABASE DATA
        binding.apply {
            val detailRatingStr = "⭐ ${movieAndTvShowDetail.voteAverage}"
            detailTitle.text = movieAndTvShowDetail.title
            detailReleaseDate.text = movieAndTvShowDetail.releaseDate
            detailRating.text = detailRatingStr
            detailOverview.text = movieAndTvShowDetail.overview

            // comment db
            viewModel.comments
                .observe(this@MovieDetailActivity) {
                    adapter.setAdapter(it)

                    // comment section
                    if (it.isNullOrEmpty()) {
                        tvNoCommentAvailable.visibility = VISIBLE
                        rvComment.visibility = GONE
                    } else {
                        tvNoCommentAvailable.visibility = GONE
                        rvComment.visibility = VISIBLE
                    }
                }

            // watchlist db
            viewModel.movieById
                .observe(this@MovieDetailActivity) {
                    if (it.isEmpty()) {
                        // add to watchlist
                        tvAddRemoveWatchList.text = "Add to Watchlist"
                        tvAddRemoveWatchList.setOnClickListener {
                            viewModel.insertMovie(movieAndTvShowDetail)
                            runOnUiThread {
                                Toast.makeText(
                                    applicationContext,
                                    "Added to watch list",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }

                    } else {
                        //remove from watchlist
                        tvAddRemoveWatchList.text = getString(R.string.remove_from_watchlist)
                        tvAddRemoveWatchList.setOnClickListener {
                            viewModel.deleteMovie(movieAndTvShowDetail)
                            runOnUiThread {
                                Toast.makeText(
                                    applicationContext,
                                    "Removed from watch list",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                }

            // REDIRECT TO COMMENT FORM
            tvWriteComment.setOnClickListener {
                CommentFormActivity.open(
                    this@MovieDetailActivity,
                    movieAndTvShowDetail.id!!,
                    WRITE_COMMENT,
                    null
                )
            }

            adapter.delegate = object : CommentDelegate {
                override fun onItemClicked(commentEntity: CommentEntity) {
                    CommentFormActivity.open(
                        this@MovieDetailActivity,
                        movieAndTvShowDetail.id!!,
                        EDIT_COMMENT,
                        commentEntity
                    )
                }
            }
        }
    }

    private fun getData(backdropPath: String?, posterPath: String?) {
        skeletonBackdrop.showSkeleton()
        skeletonPoster.showSkeleton()

        // LOAD IMAGES
        Glide.with(this)
            .load("https://image.tmdb.org/t/p/original$backdropPath")
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

        Glide.with(this)
            .load("https://image.tmdb.org/t/p/original$posterPath")
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
                    skeletonPoster.showOriginal()
                    return false
                }
            })
            .into(binding.detailPoster)
    }

    companion object {
        const val PAGE_TITLE = "page_title"
        private const val MOVIE_ID = "movie_id"
        private const val MOVIE_DETAIL = "movie_detail"

        fun open(
            activity: AppCompatActivity,
            title: String?,
            movieId: String,
            movieDetail: MovieAndTvShow
        ) {
            val intent = Intent(activity, MovieDetailActivity::class.java)
            intent.putExtra(PAGE_TITLE, title)
            intent.putExtra(MOVIE_ID, movieId)
            intent.putExtra(MOVIE_DETAIL, movieDetail)
            ActivityCompat.startActivity(activity, intent, null)
        }
    }
}