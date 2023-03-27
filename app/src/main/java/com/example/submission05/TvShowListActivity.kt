package com.example.submission03

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.submission03.databinding.ActivityTvShowListBinding
import com.example.submission05.rv_tvshow.TvShowAdapter
import com.example.submission05.rv_tvshow.TvShowDelegate
import com.example.submission05.api.RetrofitHelper
import com.example.submission05.api.TvShowsApi
import com.example.submission05.constants.Constants.Companion.AIRING_TODAY
import com.example.submission05.constants.Constants.Companion.ON_THE_AIR
import com.example.submission05.constants.Constants.Companion.POPULAR
import com.example.submission05.constants.Constants.Companion.TOP_RATED
import com.example.submission05.dialog.ErrorDialog
import com.example.submission05.dialog.LoadingDialog
import com.example.submission05.model.TvShow
import com.example.submission05.model.TvShows
import com.faltenreich.skeletonlayout.Skeleton
import com.faltenreich.skeletonlayout.applySkeleton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TvShowListActivity : AppCompatActivity() {

    private lateinit var adapter: TvShowAdapter
    private lateinit var binding: ActivityTvShowListBinding
    private lateinit var skeleton: Skeleton

    //loading dialog
    val loading = LoadingDialog()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTvShowListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // intents extra
        val title = intent.getStringExtra(TITLE)
        val section = intent.getStringExtra(SECTION)

        // title
        supportActionBar?.title = title

        // start loading
        loading.startLoading(this@TvShowListActivity)

        // setup rv and adapter
        val linearLayoutManager =
            LinearLayoutManager(this@TvShowListActivity, RecyclerView.VERTICAL, false)
        adapter = TvShowAdapter()
        binding.rvTvShow.layoutManager = linearLayoutManager
        binding.rvTvShow.adapter = adapter

        // skeleton
        skeleton = binding.rvTvShow.applySkeleton(R.layout.tvshow_item, 5)
        skeleton.showSkeleton()

        // get data
        getTvShowData(section!!)

        adapter.delegate = object : TvShowDelegate {
            override fun onItemClicked(tvShow: TvShow) {
                println(tvShow)
            }
        }
    }

    private fun getTvShowData (section: String) {
        val tvShowsApi = RetrofitHelper.getInstance().create(TvShowsApi::class.java)
        val call: Call<TvShows> = when (section) {
            POPULAR -> tvShowsApi.getPopularTvShows()
            TOP_RATED -> tvShowsApi.getTopRatedShows()
            ON_THE_AIR -> tvShowsApi.getOnTheAirTvShows()
            AIRING_TODAY -> tvShowsApi.getAiringTodayTvShows()
            else -> tvShowsApi.getPopularTvShows()
        }

        call.enqueue(object : Callback<TvShows> {
            override fun onResponse(call: Call<TvShows>, response: Response<TvShows>) {
                if (response.isSuccessful) {
                    val list = response.body()?.results
                    Log.d("blah", "onResponse: $list")
                    list?.let {
                        adapter.setAdapter(it)
                    }
                }
                Log.d("blah", "after success: ")
                skeleton.showOriginal()
                loading.endLoading()
            }

            override fun onFailure(call: Call<TvShows>, t: Throwable) {
                Log.d("foo", "onFailure: ${t.message}")
                ErrorDialog.showError(this@TvShowListActivity, "Failed to load data")
            }
        })
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