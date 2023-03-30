package com.example.submission05.ui.list_popular_people

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.GridLayoutManager
import com.example.submission05.ui.list_popular_people_detail.PopularPeopleDetailActivity
import com.example.submission03.R
import com.example.submission03.databinding.ActivityPopularPeopleListBinding
import com.example.submission05.api.PopularPeoplesApi
import com.example.submission05.api.RetrofitHelper
import com.example.submission05.utils.dialog.ErrorDialog
import com.example.submission05.utils.dialog.LoadingDialog
import com.example.submission05.data.model.PopularPeople
import com.example.submission05.data.model.PopularPeoples
import com.faltenreich.skeletonlayout.Skeleton
import com.faltenreich.skeletonlayout.applySkeleton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PopularPeopleListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPopularPeopleListBinding
    private lateinit var adapter: PopularPeopleAdapter
    private lateinit var skeleton: Skeleton

    //loading dialog
    val loading = LoadingDialog()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPopularPeopleListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Popular People"

        // recycler view adapter
        val gridLayoutManager = GridLayoutManager(this, 3)
        binding.rvPopularPeopleList.layoutManager = gridLayoutManager
        adapter = PopularPeopleAdapter()
        binding.rvPopularPeopleList.adapter = adapter

        // skeleton
        skeleton = binding.rvPopularPeopleList.applySkeleton(R.layout.popular_people_item, 12)

        // SWIPE REFRESH
        binding.swipeRefreshLayout.setOnRefreshListener {
            binding.swipeRefreshLayout.isRefreshing = false
            getApiPopularPeoples()
        }

        // get popular peoples api
        getApiPopularPeoples()

        // set adapter
        adapter.delegate = object : PopularPeopleDelegate {
            override fun onItemClicked(popularPeople: PopularPeople) {
                Log.d("foo", "onItemClicked: $popularPeople")
                PopularPeopleDetailActivity.open(
                    this@PopularPeopleListActivity,
                    popularPeople.id!!,
                    popularPeople.popularPeopleKnownFor
                )
            }
        }
    }


    private fun getApiPopularPeoples() {
        // start loading
        loading.startLoading(this@PopularPeopleListActivity)
        skeleton.showSkeleton()

        val popularPeoplesApi = RetrofitHelper.getInstance().create(PopularPeoplesApi::class.java)
        val call: Call<PopularPeoples> = popularPeoplesApi.getPopularPeoples()

        call.enqueue(object : Callback<PopularPeoples> {
            override fun onResponse(call: Call<PopularPeoples>, response: Response<PopularPeoples>) {
                if (response.isSuccessful) {
                    val list = response.body()?.results
                    list?.let {
                        Log.d("foo", "onResponse: ${list.size}")
                        adapter.setAdapter(it)
                        skeleton.showOriginal()
                        loading.endLoading()
                    }
                }
            }

            override fun onFailure(call: Call<PopularPeoples>, t: Throwable) {
                Log.d("foo", "onFailure: ${t.message}")
                ErrorDialog.showError(this@PopularPeopleListActivity, "Failed to load data")
            }

        })
    }
}