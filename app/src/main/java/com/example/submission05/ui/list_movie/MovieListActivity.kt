package com.example.submission05.ui.list_movie

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.submission05.ui.detail_movie_tvshow.MovieDetailActivity
import com.example.submission03.R
import com.example.submission03.databinding.ActivityMovieListBinding
import com.example.submission03.model.MovieAndTvShow
import com.example.submission05.utils.dialog.ErrorDialog
import com.example.submission05.utils.dialog.LoadingDialog
import com.faltenreich.skeletonlayout.Skeleton
import com.faltenreich.skeletonlayout.applySkeleton

class MovieListActivity : AppCompatActivity() {

    private lateinit var adapter: MovieAdapter
    private lateinit var binding: ActivityMovieListBinding
    private lateinit var skeleton: Skeleton
    private val viewModel: MovieListViewModel by viewModels()

    // LOADING DIALOG
    private val loading = LoadingDialog()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMovieListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // GET INTENT DATA
        val title = intent.getStringExtra(PAGE_TITLE)
        val query = intent.getStringExtra(QUERY)
        val section = intent.getStringExtra(SECTION)

        // SWIPE REFRESH
        binding.swipeRefreshLayout.setOnRefreshListener {
            binding.swipeRefreshLayout.isRefreshing = false
            Log.d("blah2", "onRefresh: dor")
            viewModel.moviesApiCall(query, section)
        }

        // action bar
        supportActionBar?.title = title

        // SETUP UI
        val linearLayoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        binding.rvMovieList.layoutManager = linearLayoutManager
        adapter = MovieAdapter()
        binding.rvMovieList.adapter = adapter

        // SKELETON
        skeleton = binding.rvMovieList.applySkeleton(R.layout.movie_item, 5)


        // GET MOVIES
        // api call
        viewModel.moviesApiCall(query, section)
        // loading
        viewModel.isLoading.observe(this) { isLoading ->
            if (isLoading) {
                loading.startLoading(this@MovieListActivity)
                skeleton.showSkeleton()
            } else {
                loading.endLoading()
                skeleton.showOriginal()
            }
        }
        // error
        viewModel.isError.observe(this) { isError ->
            if (isError) {
                loading.endLoading()
                ErrorDialog.showError(this@MovieListActivity, "Failed to load data")
            }
        }
        // get data
        viewModel.movieList.observe(this@MovieListActivity) { movies ->
            adapter.setAdapter(movies)
            skeleton.showOriginal()
            loading.endLoading()

            if (movies.isNullOrEmpty()) {
                binding.tvMovieNotFound.visibility = VISIBLE
                binding.rvMovieList.visibility = GONE
            } else {
                binding.tvMovieNotFound.visibility = GONE
                binding.rvMovieList.visibility = VISIBLE
            }
        }

        adapter.delegate = object : MovieDelegate {
            override fun onItemClicked(movieAndTvShow: MovieAndTvShow) {
                MovieDetailActivity.open(this@MovieListActivity, title, movieAndTvShow.id.toString(), "movie")
            }
        }
    }


    companion object {
        const val PAGE_TITLE = "page_title"
        const val QUERY = "QUERY"
        const val SECTION = "SECTION"

        fun open(activity: AppCompatActivity, title: String, section: String?, query: String?) {
            val intent = Intent(activity, MovieListActivity::class.java)
            intent.putExtra(PAGE_TITLE, title)
            intent.putExtra(QUERY, query)
            intent.putExtra(SECTION, section)
            ActivityCompat.startActivity(activity, intent, null)
        }
    }
}