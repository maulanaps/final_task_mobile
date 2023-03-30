package com.example.submission05.ui.list_watchlist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.VERTICAL
import com.example.submission05.ui.list_movie_tvshow_detail.MovieDetailActivity
import com.example.submission03.databinding.ActivityWatchlistBinding
import com.example.submission03.model.MovieAndTvShow
import com.example.submission05.data.room.watchlist.WatchListDatabase
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
                adapter.setAdapter(moviesEntity)
            }

            // delegate on click
            adapter.delegate = object : WatchlistShowAllDelegate {
                override fun onItemClicked(movieAndTvShow: MovieAndTvShow) {
                    MovieDetailActivity.open(
                        this@WatchlistAllActivity,
                        "Watch List Movie",
                        movieAndTvShow
                    )
                }

                override fun onDeleteBtnClicked(movieAndTvShow: MovieAndTvShow) {
                    CoroutineScope(Dispatchers.IO).launch {
                        watchlistDao.delete(movieAndTvShow)
                    }
                }
            }
        }
    }
}