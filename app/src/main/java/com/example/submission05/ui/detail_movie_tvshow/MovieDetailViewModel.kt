package com.example.submission05.ui.detail_movie_tvshow

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.*
import androidx.savedstate.SavedStateRegistryOwner
import com.example.submission03.model.MovieAndTvShow
import com.example.submission05.api.MoviesApi
import com.example.submission05.api.RetrofitHelper
import com.example.submission05.constant.Constants
import com.example.submission05.data.model.Movies
import com.example.submission05.data.room.comment.CommentDao
import com.example.submission05.data.room.watchlist.WatchListDao
import com.example.submission05.utils.DataConverter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.text.Typography.section

class MovieDetailViewModel(
    private val movieId: String,
    private val watchListDao: WatchListDao,
    private val commentDao: CommentDao
) : ViewModel() {

    // DATABASE
    val movieById = watchListDao.getMovieById(movieId)
    val comments = commentDao.getCommentsByMovieId(movieId)

    companion object {
        fun provideFactory(
            watchListDao: WatchListDao,
            commentDao: CommentDao,
            movieId: String,
            owner: SavedStateRegistryOwner,
            defaultArgs: Bundle? = null,
        ): AbstractSavedStateViewModelFactory =
            object : AbstractSavedStateViewModelFactory(owner, defaultArgs) {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(
                    key: String,
                    modelClass: Class<T>,
                    handle: SavedStateHandle
                ): T {
                    return MovieDetailViewModel(movieId, watchListDao, commentDao) as T
                }
            }
    }

    fun insertMovie(movieAndTvShow: MovieAndTvShow) {
        CoroutineScope(Dispatchers.IO).launch {
            val movieAndTvShowEntity = DataConverter.movieTvShowToEntity(movieAndTvShow)
            watchListDao.insert(movieAndTvShowEntity)
        }
    }

    fun deleteMovie(movieAndTvShow: MovieAndTvShow) {
        CoroutineScope(Dispatchers.IO).launch {
            val movieAndTvShowEntity = DataConverter.movieTvShowToEntity(movieAndTvShow)
            watchListDao.delete(movieAndTvShowEntity)
        }
    }

    // API
    private val _movieDetail = MutableLiveData<MovieAndTvShow>()
    val movieDetail: LiveData<MovieAndTvShow>
        get() = _movieDetail

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    private val _isError = MutableLiveData<Boolean>()
    val isError: LiveData<Boolean>
        get() = _isError

    fun movieDetailApiCall() {
        // INITIATE VALUE
        _isLoading.value = true
        _isError.value = false

        val moviesApi = RetrofitHelper.getInstance().create(MoviesApi::class.java)
        val call: Call<MovieAndTvShow> = moviesApi.getMovieDetail(movieId.toInt())

        call.enqueue(object : Callback<MovieAndTvShow> {
            override fun onResponse(
                call: Call<MovieAndTvShow>,
                response: Response<MovieAndTvShow>
            ) {
                if (response.isSuccessful) {
                    val movie = response.body()
                    _movieDetail.value = movie
                    _isLoading.value = false
                    Log.d("blah", "onResponse: $movie")
                }
                Log.d("foo", "after success: ")
            }

            override fun onFailure(call: Call<MovieAndTvShow>, t: Throwable) {
                Log.d("foo", "onFailure: ${t.message}")
                _isLoading.value = false
                _isError.value = true
            }
        })
    }
}