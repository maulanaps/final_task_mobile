package com.example.submission05.data.local.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "comment_table")
@Parcelize
data class CommentEntity(

    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    @ColumnInfo("movieTvShow_id") val movieTvShowId: Int,
    @ColumnInfo("writer") var writer: String,
    @ColumnInfo("content") var content: String

) : Parcelable
