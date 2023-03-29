package com.example.submission03

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.VERTICAL
import com.example.submission03.databinding.ActivityWatchlistBinding
import com.example.submission03.model.Movie
import com.example.submission05.db.DataConverter
import com.example.submission05.db.watchlist.WatchListDatabase
import com.example.submission05.rv_watchlist_showAll.WatchlistShowAllAdapter
import com.example.submission05.rv_watchlist_showAll.WatchlistShowAllDelegate
import com.faltenreich.skeletonlayout.Skeleton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

private lateinit var binding: ActivityWatchlistBinding
private lateinit var adapter: WatchlistShowAllAdapter
private lateinit var skeleton: Skeleton

class WatchlistAllActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWatchlistBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Watch List Movies"

        binding.apply {
            // rv setup
            rvWatchListAll.layoutManager =
                LinearLayoutManager(this@WatchlistAllActivity, VERTICAL, false)
            adapter = WatchlistShowAllAdapter()
            rvWatchListAll.adapter = adapter

            // get data
            val db = WatchListDatabase.getInstance(this@WatchlistAllActivity)
            val watchlistDao = db.WatchListDao()

            watchlistDao.getAll().observe(this@WatchlistAllActivity) { moviesEntity ->
                val movies = moviesEntity.map { DataConverter.entityToMovie(it) }
                adapter.setAdapter(movies)
            }

            // delegate on click
            adapter.delegate = object : WatchlistShowAllDelegate {
                override fun onItemClicked(movie: Movie) {
                    MovieDetailActivity.open(this@WatchlistAllActivity, "Watch List Movie", movie)
                }

                override fun onDeleteBtnClicked(movie: Movie) {
                    CoroutineScope(Dispatchers.IO).launch {
                        val movieEntity = DataConverter.movieToEntity(movie)
                        watchlistDao.delete(movieEntity)
                    }
                }
            }
        }
    }
}