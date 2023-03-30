package com.example.submission05.data.room.comment

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.submission05.data.entity.CommentEntity

@Dao
interface CommentDao {
    @Query("SELECT * FROM comment_table WHERE movie_id = :movieId")
    fun getCommentsByMovieId(movieId: String) : LiveData<List<CommentEntity>>

    @Insert
    fun insert(commentEntity: CommentEntity)

    @Delete
    fun delete(commentEntity: CommentEntity)

    @Update
    fun update(commentEntity: CommentEntity)
}