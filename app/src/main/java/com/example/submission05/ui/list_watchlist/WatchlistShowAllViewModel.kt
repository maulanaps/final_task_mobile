package com.example.submission05.ui.list_watchlist

import android.os.Bundle
import androidx.lifecycle.*
import androidx.savedstate.SavedStateRegistryOwner
import com.example.submission03.model.MovieAndTvShow
import com.example.submission03.model.MovieAndTvShowEntity
import com.example.submission05.data.room.watchlist.WatchListDao
import com.example.submission05.utils.DataConverter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class WatchlistShowAllViewModel(private val watchListDao: WatchListDao) : ViewModel() {
    val watchlistShowAll: LiveData<List<MovieAndTvShowEntity>> = watchListDao.getAll()

    companion object {
        fun provideFactory(
            watchListDao: WatchListDao,
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
                    return WatchlistShowAllViewModel(watchListDao) as T
                }
            }
    }

    fun deleteMovie(movieAndTvShow: MovieAndTvShow){
        CoroutineScope(Dispatchers.IO).launch {
            val movieAndTvShowEntity = DataConverter.movieTvShowToEntity(movieAndTvShow)
            watchListDao.delete(movieAndTvShowEntity)
        }
    }
}
