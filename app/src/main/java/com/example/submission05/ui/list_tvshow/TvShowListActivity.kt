package com.example.submission05.ui.list_tvshow

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.submission05.ui.detail_movie_tvshow.MovieDetailActivity
import com.example.submission03.R
import com.example.submission03.databinding.ActivityTvShowListBinding
import com.example.submission03.model.MovieAndTvShow
import com.example.submission05.ui.list_movie.MovieAdapter
import com.example.submission05.ui.list_movie.MovieDelegate
import com.example.submission05.utils.dialog.ErrorDialog
import com.example.submission05.utils.dialog.LoadingDialog
import com.faltenreich.skeletonlayout.Skeleton
import com.faltenreich.skeletonlayout.applySkeleton

class TvShowListActivity : AppCompatActivity() {

    private lateinit var adapter: MovieAdapter
    private lateinit var binding: ActivityTvShowListBinding
    private lateinit var skeleton: Skeleton
    private val viewModel: TvShowViewModel by viewModels()
    private val loading = LoadingDialog()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTvShowListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // intents extra
        val title = intent.getStringExtra(TITLE)
        val section = intent.getStringExtra(SECTION)

        // title
        supportActionBar?.title = title

        // SWIPE REFRESH
        binding.swipeRefreshLayout.setOnRefreshListener {
            binding.swipeRefreshLayout.isRefreshing = false
            Log.d("blah2", "onRefresh: dor")
            viewModel.tvShowApiCall(section!!)
        }

        // skeleton
        skeleton = binding.rvTvShow.applySkeleton(R.layout.movie_item, 5)

        // setup rv and adapter
        val linearLayoutManager =
            LinearLayoutManager(this@TvShowListActivity, RecyclerView.VERTICAL, false)
        adapter = MovieAdapter()
        binding.rvTvShow.layoutManager = linearLayoutManager
        binding.rvTvShow.adapter = adapter

        // skeleton
        skeleton = binding.rvTvShow.applySkeleton(R.layout.tvshow_item, 5)
        skeleton.showSkeleton()

        // GET TV SHOWS
        // api call
        viewModel.tvShowApiCall(section!!)
        // loading
        viewModel.isLoading.observe(this) { isLoading ->
            if (isLoading) {
                loading.startLoading(this)
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
                ErrorDialog.showError(this, "Failed to load data")
            }
        }
        // get data
        viewModel.tvShowList.observe(this) { tvShows ->
            adapter.setAdapter(tvShows)
            skeleton.showOriginal()
            loading.endLoading()
        }

        adapter.delegate = object : MovieDelegate {
            override fun onItemClicked(movieAndTvShow: MovieAndTvShow) {
                MovieDetailActivity.open(this@TvShowListActivity, "Tv Show", movieAndTvShow.id.toString(), movieAndTvShow)
                println(movieAndTvShow)
            }
        }
    }

    companion object {
        private const val TITLE = "title"
        private const val SECTION = "section"

        fun open(activity: AppCompatActivity, title: String, section: String) {
            val intent = Intent(activity, TvShowListActivity::class.java)
            intent.putExtra(TITLE, title)
            intent.putExtra(SECTION, section)
            ActivityCompat.startActivity(activity, intent, null)
        }
    }
}