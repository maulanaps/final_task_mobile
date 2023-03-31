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
import com.example.submission05.data.local.room.comment.CommentDatabase
import com.example.submission05.data.local.room.watchlist.WatchListDatabase
import com.example.submission05.data.local.entity.CommentEntity
import com.example.submission05.utils.dialog.LoadingDialog
import com.faltenreich.skeletonlayout.Skeleton

class MovieDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMovieDetailBinding
    private lateinit var adapter: CommentAdapter
    private lateinit var skeletonBackdrop: Skeleton
    private lateinit var skeletonPoster: Skeleton
    // LOADING DIALOG
    private val loading = LoadingDialog()

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
        val title = intent.getStringExtra(PAGE_TITLE)
        val mediaType = intent.getStringExtra(MEDIA_TYPE)!!

        supportActionBar?.title = title

        // RV AND ADAPTER
        val layoutManager = LinearLayoutManager(this@MovieDetailActivity, VERTICAL, false)
        binding.rvComment.layoutManager = layoutManager
        adapter = CommentAdapter()
        binding.rvComment.adapter = adapter

        // SKELETON
        skeletonBackdrop = findViewById(R.id.skeletonLayoutBackdrop)
        skeletonPoster = findViewById(R.id.skeletonLayoutPoster)

        // GET MOVIE DETAIL
        // api call
        apiCall(mediaType)
        // loading
        viewModel.isLoading.observe(this) { isLoading ->
            if (isLoading) {
                loading.startLoading(this)
                skeletonBackdrop.showSkeleton()
                skeletonPoster.showSkeleton()
            } else {
                loading.endLoading()
            }
        }
        // error
        viewModel.isError.observe(this) { isError ->
            if (isError) {
                loading.endLoading()
                ErrorDialog.showError(this, "Failed to load data")
            }
        }
        // get data
        viewModel.movieDetail.observe(this) { movieDetail ->
            loadImages(movieDetail.backdropPath, movieDetail.posterPath)

            binding.apply {
                val detailRatingStr = "⭐ ${movieDetail.voteAverage}"
                detailTitle.text = movieDetail.title
                detailReleaseDate.text = movieDetail.releaseDate
                detailRating.text = detailRatingStr
                detailOverview.text = movieDetail.overview
            }
        }

        // SWIPE REFRESH
        binding.swipeRefreshLayout.setOnRefreshListener {
            binding.swipeRefreshLayout.isRefreshing = false
            Log.d("blah2", "onRefresh: dor")
            // api call
            apiCall(mediaType)
        }

        // GET DATABASE DATA
        binding.apply {

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
            viewModel.movieTvShowById
                .observe(this@MovieDetailActivity) {
                    if (it.isEmpty()) {
                        // add to watchlist
                        tvAddRemoveWatchList.text = getString(R.string.AddtoWatchlist)
                        tvAddRemoveWatchList.setOnClickListener {

                            val newItem = viewModel.movieDetail.value!!
                            if (newItem.title == null){
                                newItem.mediaType = "tv"
                            } else {
                                newItem.mediaType = "movie"
                            }

                            Log.d("blah3", "onCreate: $newItem")

                            viewModel.insertMovie(newItem)
                            Toast.makeText(
                                applicationContext,
                                "Added to watch list",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                    } else {
                        //remove from watchlist
                        tvAddRemoveWatchList.text = getString(R.string.remove_from_watchlist)
                        tvAddRemoveWatchList.setOnClickListener {
                            viewModel.deleteMovie(viewModel.movieDetail.value!!)
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
                    viewModel.movieDetail.value?.id!!,
                    WRITE_COMMENT,
                    null
                )
            }

            adapter.delegate = object : CommentDelegate {
                override fun onItemClicked(commentEntity: CommentEntity) {
                    CommentFormActivity.open(
                        this@MovieDetailActivity,
                        viewModel.movieDetail.value?.id!!,
                        EDIT_COMMENT,
                        commentEntity
                    )
                }
            }
        }
    }

    private fun apiCall(mediaType: String){
        if (mediaType == "movie") {
            viewModel.movieDetailApiCall()
        } else if (mediaType == "tv"){
            viewModel.tvShowDetailApiCall()
        }
    }
    private fun loadImages(backdropPath: String?, posterPath: String?) {
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
        private const val MEDIA_TYPE = "media_type"

        fun open(
            activity: AppCompatActivity,
            title: String?,
            movieId: String,
            mediaType: String
        ) {
            val intent = Intent(activity, MovieDetailActivity::class.java)
            intent.putExtra(PAGE_TITLE, title)
            intent.putExtra(MOVIE_ID, movieId)
            intent.putExtra(MEDIA_TYPE, mediaType)
            ActivityCompat.startActivity(activity, intent, null)
        }
    }
}