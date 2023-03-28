package com.example.submission05.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "comment_table")
data class Comment(

    @PrimaryKey val id: Int,
    @ColumnInfo("movie_id") val movieId: Int,
    @ColumnInfo("writer") val writer: String,
    @ColumnInfo("content") val content: String

)
