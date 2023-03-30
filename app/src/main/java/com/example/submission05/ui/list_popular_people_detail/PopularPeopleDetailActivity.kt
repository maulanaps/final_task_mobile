package com.example.submission05.ui.list_popular_people_detail

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
import com.example.submission05.ui.list_movie_tvshow_detail.MovieDetailActivity
import com.example.submission05.ui.main.WatchlistAdapter
import com.example.submission05.ui.main.WatchlistDelegate
import com.faltenreich.skeletonlayout.Skeleton
import com.faltenreich.skeletonlayout.applySkeleton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDate
import java.time.Period

private lateinit var binding: ActivityPopularPeopleDetailBinding
private lateinit var adapter: WatchlistAdapter
private lateinit var knownFor: ArrayList<MovieAndTvShow>
private lateinit var skeleton: Skeleton
private lateinit var skeletonProfileImg: Skeleton

class PopularPeopleDetailActivity : AppCompatActivity() {

    private val loading = LoadingDialog()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPopularPeopleDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // TITLE
        supportActionBar?.title = "People Details"

        // INTENT DATA
        val peopleId = intent.getIntExtra(PEOPLE_ID, 0)
        knownFor  = intent.getParcelableArrayListExtra(KNOWN_FOR)!!

        // SWIPE REFRESH
        binding.swipeRefresh.setOnRefreshListener {
            binding.swipeRefresh.isRefreshing = false
            getApiPeopleDetail(peopleId)
        }

        // SETUP RECYCLER VIEW
        adapter = WatchlistAdapter()
        binding.rvPeopleKnowFor.adapter = adapter

        // START SKELETON
        skeleton = binding.rvPeopleKnowFor.applySkeleton(R.layout.movie_item, 4)
        skeletonProfileImg = findViewById(R.id.skeletonPeopleProfilePic)

        // GET DATA
        getApiPeopleDetail(peopleId)

        // OVERRIDE RV ONCLICK
        adapter.delegate = object : WatchlistDelegate {
            override fun onItemClicked(movieAndTvShow: MovieAndTvShow) {
                MovieDetailActivity.open(this@PopularPeopleDetailActivity, movieAndTvShow.title, movieAndTvShow)
            }
        }
    }

    private fun getApiPeopleDetail(peopleId: Int) {
        // START LOADING
        loading.startLoading(this@PopularPeopleDetailActivity)
        skeleton.showSkeleton()
        skeletonProfileImg.showSkeleton()

        val peopleDetailApi = RetrofitHelper.getInstance().create(PopularPeoplesApi::class.java)
        val call = peopleDetailApi.getPeopleDetail(peopleId)

        call.enqueue(object : Callback<PopularPeopleDetail> {
            override fun onResponse(
                call: Call<PopularPeopleDetail>,
                response: Response<PopularPeopleDetail>
            ) {
                if (response.isSuccessful) {
                    binding.apply {
                        val peopleDetail = response.body()
                        val genderAndRole = getGender(peopleDetail?.gender!!) + ", ${peopleDetail.knownForDepartment}"

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
                }
                loading.endLoading()
            }

            override fun onFailure(call: Call<PopularPeopleDetail>, t: Throwable) {
                ErrorDialog.showError(this@PopularPeopleDetailActivity, "Failed to load data")
            }
        })
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