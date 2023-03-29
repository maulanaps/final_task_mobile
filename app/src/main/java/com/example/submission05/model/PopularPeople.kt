package com.example.submission05.model

import com.example.submission03.model.MovieAndTvShow
import com.google.gson.annotations.SerializedName

data class PopularPeople(
    @SerializedName("adult"                ) var adult              : Boolean?            = null,
    @SerializedName("gender"               ) var gender             : Int?                = null,
    @SerializedName("id"                   ) var id                 : Int?                = null,
    @SerializedName("known_for"            ) var popularPeopleKnownFor           : ArrayList<MovieAndTvShow> = arrayListOf(),
    @SerializedName("known_for_department" ) var knownForDepartment : String?             = null,
    @SerializedName("name"                 ) var name               : String?             = null,
    @SerializedName("popularity"           ) var popularity         : Double?             = null,
    @SerializedName("profile_path"         ) var profilePath        : String?             = null
)
