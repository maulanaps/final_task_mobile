package com.example.submission03

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.submission03.databinding.ActivityMovieListBinding
import com.example.submission03.model.Movie
import com.example.submission03.movie.MovieAdapter
import com.example.submission03.movie.MovieDelegate
import com.example.submission05.constants.Constants.Companion.NOW_PLAYING
import com.example.submission05.constants.Constants.Companion.POPULAR
import com.example.submission05.constants.Constants.Companion.TOP_RATED
import com.example.submission05.constants.Constants.Companion.UPCOMING
import com.example.submission05.api.MoviesApi
import com.example.submission05.api.RetrofitHelper
import com.example.submission05.dialog.ErrorDialog
import com.example.submission05.dialog.LoadingDialog
import com.example.submission05.model.Movies
import com.faltenreich.skeletonlayout.Skeleton
import com.faltenreich.skeletonlayout.applySkeleton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MovieListActivity : AppCompatActivity() {

    private lateinit var adapter: MovieAdapter
    private lateinit var binding: ActivityMovieListBinding
    private lateinit var skeleton: Skeleton

    //loading dialog
    val loading = LoadingDialog()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMovieListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // start loading
        loading.startLoading(this@MovieListActivity)

        // get intent data
        val title = intent.getStringExtra(PAGE_TITLE)
        val query = intent.getStringExtra(QUERY)
        val section = intent.getStringExtra(SECTION)

        // action bar
        supportActionBar?.title = title

        // setup UI
        val linearLayoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        binding.rvMovieList.layoutManager = linearLayoutManager
        adapter = MovieAdapter()
        binding.rvMovieList.adapter = adapter

        // skeleton
        skeleton = binding.rvMovieList.applySkeleton(R.layout.movie_item, 5)
        skeleton.showSkeleton()

        // get movies
        getApiMovies(query, section)

        adapter.delegate = object : MovieDelegate {
            override fun onItemClicked(movie: Movie) {
                MovieDetailActivity.open(this@MovieListActivity, title, movie)
            }
        }
    }

    private fun getApiMovies(query: String?, section: String?) {
        val moviesApi = RetrofitHelper.getInstance().create(MoviesApi::class.java)
        val call: Call<Movies>
        if (query != null) {
            // search movies
            println("query not null: $query")
            call = moviesApi.searchMovies(query)
        } else {
            // get movies based on section
            println("query null")
            println("section: $section")
            call = when (section) {
                TOP_RATED -> moviesApi.getTopRatedMovies()
                UPCOMING -> moviesApi.getUpcomingMovies()
                NOW_PLAYING -> moviesApi.getNowPlayingMovies()
                POPULAR -> moviesApi.getPopularMovies()
                else -> moviesApi.getTopRatedMovies()
            }
        }

        call.enqueue(object : Callback<Movies> {
            override fun onResponse(call: Call<Movies>, response: Response<Movies>) {
                if (response.isSuccessful) {
                    val list = response.body()?.results
                    Log.d("blah", "onResponse: $list")
                    list?.let {
                        Log.d("blah", "onResponse: ${list.size}")
                        adapter.setAdapter(it)
                    }
                }
                Log.d("foo", "after success: ")
                skeleton.showOriginal()
                loading.endLoading()
            }

            override fun onFailure(call: Call<Movies>, t: Throwable) {
                Log.d("foo", "onFailure: ${t.message}")
                ErrorDialog.showError(this@MovieListActivity, "Failed to load data")
            }
        })
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