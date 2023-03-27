package com.example.submission03.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity (tableName = "favorite_table")
data class Movie (

    @SerializedName("id"                ) @PrimaryKey var id               : Int?           = null,
    @SerializedName("adult"             ) @ColumnInfo("adult"             ) var adult            : Boolean?       = null,
    @SerializedName("backdrop_path"     ) @ColumnInfo("backdrop_path"     ) var backdropPath     : String?        = null,
    @SerializedName("original_language" ) @ColumnInfo("original_language" ) var originalLanguage : String?        = null,
    @SerializedName("original_title"    ) @ColumnInfo("original_title"    ) var originalTitle    : String?        = null,
    @SerializedName("overview"          ) @ColumnInfo("overview"          ) var overview         : String?        = null,
    @SerializedName("popularity"        ) @ColumnInfo("popularity"        ) var popularity       : Double?        = null,
    @SerializedName("poster_path"       ) @ColumnInfo("poster_path"       ) var posterPath       : String?        = null,
    @SerializedName("release_date"      ) @ColumnInfo("release_date"      ) var releaseDate      : String?        = null,
    @SerializedName("title"             ) @ColumnInfo("title"             ) var title            : String?        = null,
    @SerializedName("video"             ) @ColumnInfo("video"             ) var video            : Boolean?       = null,
    @SerializedName("vote_average"      ) @ColumnInfo("vote_average"      ) var voteAverage      : Double?        = null,
    @SerializedName("vote_count"        ) @ColumnInfo("vote_count"        ) var voteCount        : Int?           = null

) : Parcelable


//@Parcelize
//data class Movie(
//    var backdrop      : Int,
//    var id            : Int,
//    var originalTitle : String,
//    var overview      : String,
//    var popularity    : Double,
//    var poster        : Int,
//    var releaseDate   : String,
//    var title         : String,
//    var voteAverage   : Double,
//    var voteCount     : Int
//) : Parcelable
