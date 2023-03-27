package com.example.submission03

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.submission03.databinding.ActivityFavoriteListBinding
import com.example.submission03.model.Movie
import com.example.submission03.movie.MovieAdapter
import com.example.submission03.movie.MovieDelegate
import com.example.submission05.db.AppDatabase

class FavoriteListActivity : AppCompatActivity() {

    private lateinit var adapter: MovieAdapter
    private lateinit var binding: ActivityFavoriteListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Favorite Movies"

        // setup rv and adapter
        val linearLayoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        binding.rvFavoriteList.layoutManager = linearLayoutManager
        adapter = MovieAdapter()
        binding.rvFavoriteList.adapter = adapter

        // get data
        val db = AppDatabase.getInstance(this)
        db
            .roomMovieDao()
            .getAll()
            .observe(this@FavoriteListActivity) { favoriteMovies ->
                Log.d("bar", "onResponse: $favoriteMovies")
                adapter.setAdapter(favoriteMovies)
            }

        adapter.delegate = object : MovieDelegate {
            override fun onItemClicked(movie: Movie) {
                MovieDetailActivity.open(this@FavoriteListActivity, "Favorite Movie", movie)
            }
        }
    }

    companion object {
        fun open(activity: AppCompatActivity) {
            val intent = Intent(activity, FavoriteListActivity::class.java)
            ActivityCompat.startActivity(activity, intent, null)
        }
    }
}