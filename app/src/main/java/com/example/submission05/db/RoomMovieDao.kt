package com.example.submission05.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.submission03.model.Movie

@Dao
interface RoomMovieDao {
    @Query("SELECT * FROM favorite_table")
    fun getAll(): LiveData<List<Movie>>

    @Query("SELECT * FROM favorite_table WHERE id = :id")
    fun getFavoriteById(id: String) : LiveData<List<Movie>>

    @Insert (onConflict = OnConflictStrategy.IGNORE)
    fun insert(movie: Movie)

    @Delete
    fun delete(movie: Movie)
}