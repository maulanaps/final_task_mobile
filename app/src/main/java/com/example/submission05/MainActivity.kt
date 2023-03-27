package com.example.submission03

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import com.example.submission03.databinding.ActivityMainBinding
import com.example.submission05.constants.Constants.Companion.AIRING_TODAY
import com.example.submission05.constants.Constants.Companion.NOW_PLAYING
import com.example.submission05.constants.Constants.Companion.ON_THE_AIR
import com.example.submission05.constants.Constants.Companion.POPULAR
import com.example.submission05.constants.Constants.Companion.TOP_RATED
import com.example.submission05.constants.Constants.Companion.UPCOMING
import com.example.submission05.dialog.ErrorDialog

@SuppressLint("StaticFieldLeak")
private lateinit var binding: ActivityMainBinding

class MainActivity : AppCompatActivity() {
    @SuppressLint("UseCompatLoadingForDrawables")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Main Menu"

        // search
        binding.ivSearchBtn.setOnClickListener {
            val query = binding.etSearchText.text.toString()
            println(query)
            if (query.isBlank()) {
                val builder = AlertDialog.Builder(this@MainActivity)
                builder.setMessage("Please write the movie's title")
                builder.setPositiveButton(android.R.string.ok) { dialog, which ->
                    binding.etSearchText.setText("")
                }
                builder.show()
            } else {
                MovieListActivity.open(this@MainActivity, "Top Rated Movies", null, query)
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

        // FAVORITES
        binding.favoriteMoviesFavoriteMovie.setOnClickListener {
            FavoriteListActivity.open(this@MainActivity)
//            ErrorDialog.showError(this@MainActivity, "Failed to load data")
        }
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
        sharedPref.edit().clear().apply()

        // redirect to login activity
        LoginActivity.open(this@MainActivity)
    }
}