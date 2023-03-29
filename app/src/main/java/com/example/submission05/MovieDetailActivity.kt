package com.example.submission03

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import androidx.recyclerview.widget.RecyclerView.VERTICAL
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.submission03.databinding.ActivityMovieDetailBinding
import com.example.submission03.model.Movie
import com.example.submission05.constants.Constants.Companion.EDIT_COMMENT
import com.example.submission05.constants.Constants.Companion.WRITE_COMMENT
import com.example.submission05.dialog.ErrorDialog
import com.example.submission05.db.DataConverter
import com.example.submission05.db.comment.CommentDatabase
import com.example.submission05.db.watchlist.WatchListDatabase
import com.example.submission05.model.CommentEntity
import com.example.submission05.rv_comment.CommentAdapter
import com.example.submission05.rv_comment.CommentDelegate
import com.faltenreich.skeletonlayout.Skeleton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

private lateinit var binding: ActivityMovieDetailBinding
private lateinit var adapter: CommentAdapter

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

        // RV AND ADAPTER
        val layoutManager = LinearLayoutManager(this@MovieDetailActivity, VERTICAL, false)
        binding.rvComment.layoutManager = layoutManager
        adapter = CommentAdapter()
        binding.rvComment.adapter = adapter

        // SKELETON
        val skeletonBackdrop: Skeleton = findViewById(R.id.skeletonLayoutBackdrop)
        val skeletonPoster: Skeleton = findViewById(R.id.skeletonLayoutPoster)
        skeletonBackdrop.showSkeleton()
        skeletonPoster.showSkeleton()

        // GET DATA
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

            // watchlist db
            val watchListDb = WatchListDatabase.getInstance(this@MovieDetailActivity)
            val watchListDao = watchListDb.WatchListDao()
            watchListDao
                .getMovieById(movieDetail.id.toString())
                .observe(this@MovieDetailActivity) {
                    if (it.isEmpty()) {
                        tvAddWatchList.visibility = View.VISIBLE
                        tvRemoveWatchList.visibility = View.GONE
                    } else {
                        tvAddWatchList.visibility = View.GONE
                        tvRemoveWatchList.visibility = View.VISIBLE
                    }
                }

            // comment db
            val commentDb = CommentDatabase.getInstance(this@MovieDetailActivity)
            val commentDao = commentDb.CommentDao()
            commentDao
                .getCommentsByMovieId(movieDetail.id.toString())
                .observe(this@MovieDetailActivity) {
                    adapter.setAdapter(it)
                }

            tvAddWatchList.setOnClickListener {
                CoroutineScope(Dispatchers.IO).launch {
                    val movieEntity = DataConverter.movieToEntity(movieDetail)
                    watchListDao.insert(movieEntity)
                    runOnUiThread {
                        Toast.makeText(applicationContext, "Added to watch list", Toast.LENGTH_SHORT).show()
                    }
                    Log.d("blah", "addWatchList: movie ${movieDetail.title} saved to watch list")
                }
            }

            tvRemoveWatchList.setOnClickListener {
                CoroutineScope(Dispatchers.IO).launch {
                    val movieEntity = DataConverter.movieToEntity(movieDetail)
                    watchListDao.delete(movieEntity)
                    runOnUiThread {
                        Toast.makeText(applicationContext, "Removed from watch list", Toast.LENGTH_SHORT).show()
                    }
                    Log.d("blah", "removeWatchList: movie ${movieDetail.title} removed to watch list")
                }
            }

            tvWriteComment.setOnClickListener {
                CommentFormActivity.open(this@MovieDetailActivity, movieDetail.id!!, WRITE_COMMENT, null)
            }

            adapter.delegate = object : CommentDelegate {
                override fun onItemClicked(commentEntity: CommentEntity) {
                    CommentFormActivity.open(this@MovieDetailActivity, movieDetail.id!!, EDIT_COMMENT, commentEntity)
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