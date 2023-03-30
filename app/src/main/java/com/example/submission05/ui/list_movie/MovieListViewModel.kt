package com.example.submission05.ui.list_movie

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.submission03.model.MovieAndTvShow
import com.example.submission05.api.MoviesApi
import com.example.submission05.api.RetrofitHelper
import com.example.submission05.constant.Constants
import com.example.submission05.data.model.Movies
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MovieListViewModel : ViewModel() {
    private val _movieList = MutableLiveData<List<MovieAndTvShow>>()
    val movieList: LiveData<List<MovieAndTvShow>>
        get() = _movieList

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    private val _isError = MutableLiveData<Boolean>()
    val isError: LiveData<Boolean>
        get() = _isError

    fun moviesApiCall(query: String?, section: String?) {
        // INITIATE VALUE
        _isLoading.value = true
        _isError.value = false

        val moviesApi = RetrofitHelper.getInstance().create(MoviesApi::class.java)
        val call: Call<Movies>
        if (query != null) {
            // search movies
            call = moviesApi.searchMovies(query)
        } else {
            // get movies based on section
            call = when (section) {
                Constants.TOP_RATED -> moviesApi.getTopRatedMovies()
                Constants.UPCOMING -> moviesApi.getUpcomingMovies()
                Constants.NOW_PLAYING -> moviesApi.getNowPlayingMovies()
                Constants.POPULAR -> moviesApi.getPopularMovies()
                else -> moviesApi.getTopRatedMovies()
            }
        }

        call.enqueue(object : Callback<Movies> {
            override fun onResponse(call: Call<Movies>, response: Response<Movies>) {
                if (response.isSuccessful) {
                    val list = response.body()?.results
                    Log.d("blah", "onResponse: $list")
                    list?.let {
                        Log.d("blah", "onResponse: ${list.size}")
                        _movieList.value = it
                        _isLoading.value = false
                    }
                }
                Log.d("foo", "after success: ")
            }

            override fun onFailure(call: Call<Movies>, t: Throwable) {
                Log.d("foo", "onFailure: ${t.message}")
                _isLoading.value = false
                _isError.value = true
            }
        })
    }
}