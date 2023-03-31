package com.example.submission05.ui.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView.*
import com.example.submission03.*
import com.example.submission03.databinding.ActivityMainBinding
import com.example.submission03.model.MovieAndTvShow
import com.example.submission05.constant.Constants.Companion.AIRING_TODAY
import com.example.submission05.constant.Constants.Companion.NOW_PLAYING
import com.example.submission05.constant.Constants.Companion.ON_THE_AIR
import com.example.submission05.constant.Constants.Companion.POPULAR
import com.example.submission05.constant.Constants.Companion.TOP_RATED
import com.example.submission05.constant.Constants.Companion.UPCOMING
import com.example.submission05.data.local.room.watchlist.WatchListDatabase
import com.example.submission05.ui.list_movie.MovieListActivity
import com.example.submission05.ui.detail_movie_tvshow.MovieDetailActivity
import com.example.submission05.ui.list_popular_people.PopularPeopleListActivity
import com.example.submission05.ui.list_tvshow.TvShowListActivity
import com.example.submission05.ui.list_watchlist.WatchlistAllActivity
import com.example.submission05.ui.list_watchlist.WatchlistShowAllViewModel
import com.example.submission05.ui.login.LoginActivity
import com.example.submission05.utils.DataConverter


class MainActivity : AppCompatActivity() {

    private lateinit var adapter: WatchlistAdapter
    private lateinit var binding: ActivityMainBinding

    // VIEW MODEL
    private val viewModel: WatchlistShowAllViewModel by viewModels {
        val watchListDao = WatchListDatabase.getInstance(this@MainActivity).WatchListDao()
        WatchlistShowAllViewModel.provideFactory(watchListDao, this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Main Menu"

        // SEARCH BUTTON
        binding.ivSearchBtn.setOnClickListener {
            val query = binding.etSearchText.text.toString()
            println(query)
            if (query.isBlank()) {
                val builder = AlertDialog.Builder(this@MainActivity)
                builder.setMessage("Please write the movie's title")
                builder.setPositiveButton(android.R.string.ok) { _, _ ->
                    binding.etSearchText.setText("")
                }
                builder.show()
            } else {
                MovieListActivity.open(this@MainActivity, "Searched Movies", null, query)
            }
        }

        // MOVIES
        binding.movieTopRated.setOnClickListener {
            MovieListActivity.open(this@MainActivity, "Top Rated Movies", TOP_RATED, null)
        }
        binding.movieUpcoming.setOnClickListener {
            MovieListActivity.open(this@MainActivity, "Upcoming Movies", UPCOMING, null)
        }
        binding.movieNowPlaying.setOnClickListener {
            MovieListActivity.open(this@MainActivity, "Now Playing Movies", NOW_PLAYING, null)
        }
        binding.moviePopular.setOnClickListener {
            MovieListActivity.open(this@MainActivity, "Popular Movies", POPULAR, null)
        }

        // TV SHOWS
        binding.tvShowsPopular.setOnClickListener {
            TvShowListActivity.open(this@MainActivity, "Popular TV Shows", POPULAR)
        }
        binding.tvShowsTopRated.setOnClickListener {
            TvShowListActivity.open(this@MainActivity, "Top Rated TV Shows", TOP_RATED)
        }
        binding.tvShowsOnTheAir.setOnClickListener {
            TvShowListActivity.open(this@MainActivity, "On The Air TV Shows", ON_THE_AIR)
        }
        binding.tvShowsAiringToday.setOnClickListener {
            TvShowListActivity.open(this@MainActivity, "Airing Today TV Shows", AIRING_TODAY)
        }

        // POPULAR PEOPLES
        binding.popularPeoplesPopularPeople.setOnClickListener {
            val intent = Intent(this@MainActivity, PopularPeopleListActivity::class.java)
            startActivity(intent)
        }

        // WATCHLIST SETUP
        adapter = WatchlistAdapter()
        binding.rvWatchList.adapter = adapter

        // GET WATCHLIST DATA
        viewModel.watchlistShowAll.observe(this@MainActivity) { watchlistMoviesEntity ->

            val watchlistMovies =
                watchlistMoviesEntity.map { DataConverter.entityToMovieTvShow(it) }
            if (watchlistMovies.isNullOrEmpty()) {
                binding.tvWatchlistEmpty.visibility = VISIBLE
                binding.rvWatchList.visibility = GONE
                binding.tvShowAllWatchList.setOnClickListener {
                    Log.d("blah", "onCreate: $watchlistMovies")
                    Toast.makeText(
                        this,
                        "No Watchlist Available",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else {

                adapter.setAdapter(watchlistMovies)
                binding.tvWatchlistEmpty.visibility = GONE
                binding.rvWatchList.visibility = VISIBLE
                binding.tvShowAllWatchList.setOnClickListener {
                    val intent = Intent(this@MainActivity, WatchlistAllActivity::class.java)
                    startActivity(intent)
                }
            }
        }

        // WATCHLIST ITEM ON CLICK
        adapter.delegate = object : WatchlistDelegate {
            override fun onItemClicked(movieAndTvShow: MovieAndTvShow) {
                MovieDetailActivity.open(
                    this@MainActivity,
                    "Watch List Movie",
                    movieAndTvShow.id.toString(),
                    movieAndTvShow.mediaType!!
                )
            }
        }
    }

    fun makeToast(msg: String) {

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.nav_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.logout -> logout()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onResume() {
        super.onResume()

        binding.etSearchText.setText("")
        binding.etSearchText.clearFocus()
        Log.d("foo", "onResume: resume")
    }

    private fun logout() {
        // delete shared preference login data
        val sharedPref = getSharedPreferences("LOGIN", MODE_PRIVATE)
        sharedPref.edit().putBoolean("isLoggedIn", false).apply()

        // redirect to login activity
        LoginActivity.open(this@MainActivity)
        finish()
    }
}