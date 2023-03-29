package com.example.submission03

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.*
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.submission03.databinding.ActivityMovieDetailBinding
import com.example.submission03.model.MovieAndTvShow
import com.example.submission05.constants.Constants.Companion.EDIT_COMMENT
import com.example.submission05.constants.Constants.Companion.WRITE_COMMENT
import com.example.submission05.dialog.ErrorDialog
import com.example.submission05.db.comment.CommentDatabase
import com.example.submission05.db.watchlist.WatchListDatabase
import com.example.submission05.model.CommentEntity
import com.example.submission05.rv_comment.CommentAdapter
import com.example.submission05.rv_comment.CommentDelegate
import com.faltenreich.skeletonlayout.Skeleton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.text.Typography.section

private lateinit var binding: ActivityMovieDetailBinding
private lateinit var adapter: CommentAdapter
private lateinit var skeletonBackdrop: Skeleton
private lateinit var skeletonPoster: Skeleton

class MovieDetailActivity : AppCompatActivity() {
    @SuppressLint("UseCompatLoadingForDrawables", "SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMovieDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // EXTRAS
        val movieAndTvShowDetail = intent.getParcelableExtra<MovieAndTvShow>(MOVIE_DETAIL) as MovieAndTvShow
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
            detailTitle.text = movieAndTvShowDetail.title
            detailReleaseDate.text = movieAndTvShowDetail.releaseDate
            detailRating.text = "⭐ ${movieAndTvShowDetail.voteAverage}"
            detailOverview.text = movieAndTvShowDetail.overview

            // watchlist db
            val watchListDb = WatchListDatabase.getInstance(this@MovieDetailActivity)
            val watchListDao = watchListDb.WatchListDao()
            watchListDao
                .getMovieById(movieAndTvShowDetail.id.toString())
                .observe(this@MovieDetailActivity) {
                    if (it.isEmpty()) {
                        // add to watchlist
                        tvAddRemoveWatchList.text = "Add to Watchlist"
                        tvAddRemoveWatchList.setOnClickListener {
                            CoroutineScope(Dispatchers.IO).launch {
                                watchListDao.insert(movieAndTvShowDetail)
                                runOnUiThread {
                                    Toast.makeText(applicationContext, "Added to watch list", Toast.LENGTH_SHORT).show()
                                }
                                Log.d("blah", "addWatchList: movie ${movieAndTvShowDetail.title} saved to watch list")
                            }
                        }

                    } else {
                        //remove from watchlist
                        tvAddRemoveWatchList.text = "Remove from watchlist"
                        tvAddRemoveWatchList.setOnClickListener {
                            CoroutineScope(Dispatchers.IO).launch {
                                watchListDao.delete(movieAndTvShowDetail)
                                runOnUiThread {
                                    Toast.makeText(applicationContext, "Removed from watch list", Toast.LENGTH_SHORT).show()
                                }
                                Log.d("blah", "removeWatchList: movie ${movieAndTvShowDetail.title} removed to watch list")
                            }
                        }
                    }
                }

            // comment db
            val commentDb = CommentDatabase.getInstance(this@MovieDetailActivity)
            val commentDao = commentDb.CommentDao()
            commentDao
                .getCommentsByMovieId(movieAndTvShowDetail.id.toString())
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

            tvWriteComment.setOnClickListener {
                CommentFormActivity.open(this@MovieDetailActivity, movieAndTvShowDetail.id!!, WRITE_COMMENT, null)
            }

            adapter.delegate = object : CommentDelegate {
                override fun onItemClicked(commentEntity: CommentEntity) {
                    CommentFormActivity.open(this@MovieDetailActivity, movieAndTvShowDetail.id!!, EDIT_COMMENT, commentEntity)
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
        const val MOVIE_DETAIL = "movie_detail"

        fun open(activity: AppCompatActivity, title: String?, data: MovieAndTvShow) {
            val intent = Intent(activity, MovieDetailActivity::class.java)
            intent.putExtra(PAGE_TITLE, title)
            intent.putExtra(MOVIE_DETAIL, data)
            ActivityCompat.startActivity(activity, intent, null)
        }
    }
}