package com.example.submission05.data.room.watchlist

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.submission03.model.MovieAndTvShow
import com.example.submission03.model.MovieAndTvShowEntity

@Dao
interface WatchListDao {
    @Query("SELECT * FROM watchlist_table")
    fun getAll() : LiveData<List<MovieAndTvShowEntity>>

    @Query("SELECT * FROM watchlist_table WHERE id = :id")
    fun getMovieById(id: String) : LiveData<List<MovieAndTvShowEntity>>

    @Insert
    fun insert(movieAndTvShowEntity: MovieAndTvShowEntity)

    @Delete
    fun delete(movieAndTvShowEntity: MovieAndTvShowEntity)
}