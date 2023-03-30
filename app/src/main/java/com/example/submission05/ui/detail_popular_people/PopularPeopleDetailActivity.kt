package com.example.submission05.ui.detail_popular_people

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import com.bumptech.glide.Glide
import com.example.submission03.R
import com.example.submission03.databinding.ActivityPopularPeopleDetailBinding
import com.example.submission03.model.MovieAndTvShow
import com.example.submission05.api.PopularPeoplesApi
import com.example.submission05.api.RetrofitHelper
import com.example.submission05.utils.dialog.ErrorDialog
import com.example.submission05.utils.dialog.LoadingDialog
import com.example.submission05.data.model.PopularPeopleDetail
import com.example.submission05.ui.detail_movie_tvshow.MovieDetailActivity
import com.example.submission05.ui.list_movie.MovieListViewModel
import com.example.submission05.ui.main.WatchlistAdapter
import com.example.submission05.ui.main.WatchlistDelegate
import com.faltenreich.skeletonlayout.Skeleton
import com.faltenreich.skeletonlayout.applySkeleton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDate
import java.time.Period
import kotlin.text.Typography.section


class PopularPeopleDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPopularPeopleDetailBinding
    private lateinit var adapter: WatchlistAdapter
    private lateinit var knownFor: ArrayList<MovieAndTvShow>
    private lateinit var skeleton: Skeleton
    private lateinit var skeletonProfileImg: Skeleton
    private val viewModel: PopularPeopleDetailViewModel by viewModels()
    private val loading = LoadingDialog()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPopularPeopleDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // TITLE
        supportActionBar?.title = "People Details"

        // INTENT DATA
        val peopleId = intent.getIntExtra(PEOPLE_ID, 0)
        knownFor = intent.getParcelableArrayListExtra(KNOWN_FOR)!!

        // SWIPE REFRESH
        binding.swipeRefresh.setOnRefreshListener {
            binding.swipeRefresh.isRefreshing = false
            viewModel.peopleDetailApiCall(peopleId)
        }

        // SETUP RECYCLER VIEW
        adapter = WatchlistAdapter()
        binding.rvPeopleKnowFor.adapter = adapter

        // START SKELETON
        skeleton = binding.rvPeopleKnowFor.applySkeleton(R.layout.movie_item, 4)
        skeletonProfileImg = findViewById(R.id.skeletonPeopleProfilePic)


        // GET MOVIES
        // api call
        viewModel.peopleDetailApiCall(peopleId)
        // loading
        viewModel.isLoading.observe(this) { isLoading ->
            if (isLoading) {
                loading.startLoading(this@PopularPeopleDetailActivity)
                skeleton.showSkeleton()
                skeletonProfileImg.showSkeleton()
            } else {
                loading.endLoading()
                skeleton.showOriginal()
                skeletonProfileImg.showOriginal()
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
        viewModel.peopleDetail.observe(this) { peopleDetail ->
            binding.apply {
                val genderAndRole =
                    getGender(peopleDetail?.gender!!) + ", ${peopleDetail.knownForDepartment}"

                tvPeopleName.text = peopleDetail.name
                tvGenderAndRole.text = genderAndRole
                tvBirthday.text = calcAge(peopleDetail.birthday!!, peopleDetail.deathday)
                tvBirthplace.text = peopleDetail.placeOfBirth
                tvKnownAs.text = peopleDetail.alsoKnownAs?.joinToString()
                tvBiography.text = peopleDetail.biography

                Glide.with(this@PopularPeopleDetailActivity)
                    .load("https://image.tmdb.org/t/p/original" + peopleDetail?.profilePath)
                    .placeholder(R.drawable.image_placeholder)
                    .into(ivPeopleProfilePic)

                adapter.setAdapter(knownFor.toList())
                skeleton.showOriginal()
                skeletonProfileImg.showOriginal()
            }
            skeleton.showOriginal()
            loading.endLoading()
        }

        // OVERRIDE RV ONCLICK
        adapter.delegate = object : WatchlistDelegate {
            override fun onItemClicked(movieAndTvShow: MovieAndTvShow) {
                MovieDetailActivity.open(
                    this@PopularPeopleDetailActivity,
                    movieAndTvShow.title,
                    movieAndTvShow.id.toString(),
                    movieAndTvShow
                )
            }
        }
    }

    fun getGender(genderId: Int): String {
        if (genderId == 1) {
            return "Female"
        }
        return "Male"
    }

    fun calcAge(birthday: String, deathday: String?): String {
        val localDate = LocalDate.parse(birthday)
        val age = Period.between(localDate, LocalDate.now()).years

        val result = if (deathday == null) {
            "$birthday ($age years old)"
        } else {
            "$birthday (Died on $deathday)"
        }

        return result
    }

    companion object {
        private const val KNOWN_FOR = "known_for"
        private const val PEOPLE_ID = "people_id"

        fun open(
            activity: AppCompatActivity,
            peopleId: Int,
            knownFor: ArrayList<MovieAndTvShow>
        ) {
            val intent = Intent(activity, PopularPeopleDetailActivity::class.java)
            intent.putParcelableArrayListExtra(KNOWN_FOR, knownFor)
            intent.putExtra(PEOPLE_ID, peopleId)
            ActivityCompat.startActivity(activity, intent, null)
        }
    }
}