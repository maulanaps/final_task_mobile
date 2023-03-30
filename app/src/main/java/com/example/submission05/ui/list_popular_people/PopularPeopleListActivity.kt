package com.example.submission05.ui.list_popular_people

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.example.submission05.ui.detail_popular_people.PopularPeopleDetailActivity
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
    private val viewModel: PopularPeopleViewModel by viewModels()

    //loading dialog
    private val loading = LoadingDialog()

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
            viewModel.popularPeopleApiCall()
        }

        // GET MOVIES
        // api call
        viewModel.popularPeopleApiCall()
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
                ErrorDialog.showError(this@PopularPeopleListActivity, "Failed to load data")
            }
        }
        // get data
        viewModel.popularPeopleList.observe(this) { movies ->
            adapter.setAdapter(movies)
            skeleton.showOriginal()
            loading.endLoading()
        }

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

}