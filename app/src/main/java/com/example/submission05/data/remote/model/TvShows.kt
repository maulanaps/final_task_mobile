package com.example.submission05.data.remote.model

import com.google.gson.annotations.SerializedName

data class TvShows(
    @SerializedName("page"          ) var page         : Int?               = null,
    @SerializedName("results"       ) var results      : List<TvShow>       = listOf(),
    @SerializedName("total_pages"   ) var totalPages   : Int?               = null,
    @SerializedName("total_results" ) var totalResults : Int?               = null
)
