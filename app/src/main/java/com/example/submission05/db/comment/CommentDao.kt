package com.example.submission05.db.watchlist

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.submission03.model.Movie
import com.example.submission03.model.MovieEntity

@Dao
interface WatchListDao {
    @Query("SELECT * FROM watchlist_table")
    fun getAll() : LiveData<List<MovieEntity>>

    @Query("SELECT * FROM watchlist_table WHERE id = :id")
    fun getMovieById(id: String) : LiveData<List<MovieEntity>>

    @Insert
    fun insert(movie: MovieEntity)

    @Delete
    fun delete(movie: MovieEntity)
}