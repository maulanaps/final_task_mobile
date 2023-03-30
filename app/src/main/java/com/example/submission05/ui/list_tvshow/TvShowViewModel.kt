package com.example.submission05.ui.list_tvshow

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.submission03.model.MovieAndTvShow
import com.example.submission05.api.RetrofitHelper
import com.example.submission05.api.TvShowsApi
import com.example.submission05.constant.Constants
import com.example.submission05.data.model.Movies
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TvShowViewModel : ViewModel() {
    private val _tvShowList = MutableLiveData<List<MovieAndTvShow>>()
    val tvShowList: LiveData<List<MovieAndTvShow>>
        get() = _tvShowList

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    private val _isError = MutableLiveData<Boolean>()
    val isError: LiveData<Boolean>
        get() = _isError

    fun tvShowApiCall (section: String) {
        // INITIATE VALUE
        _isLoading.value = true
        _isError.value = false

        val tvShowsApi = RetrofitHelper.getInstance().create(TvShowsApi::class.java)
        val call: Call<Movies> = when (section) {
            Constants.POPULAR -> tvShowsApi.getPopularTvShows()
            Constants.TOP_RATED -> tvShowsApi.getTopRatedShows()
            Constants.ON_THE_AIR -> tvShowsApi.getOnTheAirTvShows()
            Constants.AIRING_TODAY -> tvShowsApi.getAiringTodayTvShows()
            else -> tvShowsApi.getPopularTvShows()
        }

        call.enqueue(object : Callback<Movies> {
            override fun onResponse(call: Call<Movies>, response: Response<Movies>) {
                if (response.isSuccessful) {
                    val list = response.body()?.results
                    Log.d("blah", "onResponse: $list")
                    list?.let {
                        _tvShowList.value = it
                        _isLoading.value = false
                    }
                }
            }

            override fun onFailure(call: Call<Movies>, t: Throwable) {
                Log.d("foo", "onFailure: ${t.message}")
                _isLoading.value = false
                _isError.value = true
            }
        })
    }
}