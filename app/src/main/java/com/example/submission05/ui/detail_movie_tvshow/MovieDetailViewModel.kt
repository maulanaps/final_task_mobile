package com.example.submission05.ui.detail_movie_tvshow

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.*
import androidx.savedstate.SavedStateRegistryOwner
import androidx.test.internal.runner.junit4.statement.UiThreadStatement.runOnUiThread
import com.example.submission03.model.MovieAndTvShow
import com.example.submission03.model.MovieAndTvShowEntity
import com.example.submission05.data.room.comment.CommentDao
import com.example.submission05.data.room.watchlist.WatchListDao
import com.example.submission05.utils.DataConverter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MovieDetailViewModel(private val movieId: String, private val watchListDao: WatchListDao, private val commentDao: CommentDao) : ViewModel() {

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

    // MOVIE API
    private val _movieDetail = MutableLiveData<MovieAndTvShow>()
    val movieDetail: LiveData<MovieAndTvShow>
        get() = _movieDetail

    private val _isInserted = MutableLiveData<Boolean>()
    val isInserted: LiveData<Boolean>
        get() = _isInserted

    private val _isDeleted = MutableLiveData<Boolean>()
    val isDeleted: LiveData<Boolean>
        get() = _isDeleted


    fun insertMovie(movieAndTvShow: MovieAndTvShow){
        CoroutineScope(Dispatchers.IO).launch {
            val movieAndTvShowEntity = DataConverter.movieTvShowToEntity(movieAndTvShow)
            watchListDao.insert(movieAndTvShowEntity)
        }
    }

    fun deleteMovie(movieAndTvShow: MovieAndTvShow){
        CoroutineScope(Dispatchers.IO).launch {
            val movieAndTvShowEntity = DataConverter.movieTvShowToEntity(movieAndTvShow)
            watchListDao.delete(movieAndTvShowEntity)
        }
    }
}