package com.example.submission03.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Entity (tableName = "watchlist_table")
data class MovieEntity (

    @PrimaryKey                             var id               : Int?           = null,
    @ColumnInfo("adult"             ) var adult            : Boolean?       = null,
    @ColumnInfo("backdrop_path"     ) var backdropPath     : String?        = null,
    @ColumnInfo("original_language" ) var originalLanguage : String?        = null,
    @ColumnInfo("original_title"    ) var originalTitle    : String?        = null,
    @ColumnInfo("overview"          ) var overview         : String?        = null,
    @ColumnInfo("popularity"        ) var popularity       : Double?        = null,
    @ColumnInfo("poster_path"       ) var posterPath       : String?        = null,
    @ColumnInfo("release_date"      ) var releaseDate      : String?        = null,
    @ColumnInfo("title"             ) var title            : String?        = null,
    @ColumnInfo("video"             ) var video            : Boolean?       = null,
    @ColumnInfo("vote_average"      ) var voteAverage      : Double?        = null,
    @ColumnInfo("vote_count"        ) var voteCount        : Int?           = null

)
