package com.example.submission05.ui.list_watchlist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View.GONE
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.VERTICAL
import androidx.recyclerview.widget.RecyclerView.VISIBLE
import com.example.submission05.ui.detail_movie_tvshow.MovieDetailActivity
import com.example.submission03.databinding.ActivityWatchlistBinding
import com.example.submission03.model.MovieAndTvShow
import com.example.submission05.data.room.watchlist.WatchListDatabase
import com.example.submission05.utils.DataConverter
import com.faltenreich.skeletonlayout.Skeleton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class WatchlistAllActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWatchlistBinding
    private lateinit var adapter: WatchlistShowAllAdapter

    // VIEW MODEL
    private val viewModel: WatchlistShowAllViewModel by viewModels {
        val watchListDao = WatchListDatabase.getInstance(this).WatchListDao()
        WatchlistShowAllViewModel.provideFactory(watchListDao, this)
    }

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

            viewModel.watchlistShowAll.observe(this@WatchlistAllActivity) { moviesEntity ->

                val movies = moviesEntity.map { DataConverter.entityToMovieTvShow(it) }

                if (movies.isNullOrEmpty()) {
                    tvWatchlistEmpty.visibility = VISIBLE
                    rvWatchListAll.visibility = GONE
                } else {
                    adapter.setAdapter(movies)
                    tvWatchlistEmpty.visibility = GONE
                    rvWatchListAll.visibility = VISIBLE
                }
            }

            // delegate on click
            adapter.delegate = object : WatchlistShowAllDelegate {
                override fun onItemClicked(movieAndTvShow: MovieAndTvShow) {
                    MovieDetailActivity.open(
                        this@WatchlistAllActivity,
                        "Watch List Movie",
                        movieAndTvShow.id.toString(),
                        movieAndTvShow
                    )
                }

                override fun onDeleteBtnClicked(movieAndTvShow: MovieAndTvShow) {
                    CoroutineScope(Dispatchers.IO).launch {
                        viewModel.deleteMovie(movieAndTvShow)
                    }
                }
            }
        }
    }
}