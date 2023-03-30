package com.example.submission05.data.model

import com.google.gson.annotations.SerializedName

data class PopularPeoples(
    @SerializedName("page"          ) var page         : Int?                       = null,
    @SerializedName("results"       ) var results      : ArrayList<PopularPeople>   = arrayListOf(),
    @SerializedName("total_pages"   ) var totalPages   : Int?                       = null,
    @SerializedName("total_results" ) var totalResults : Int?                       = null
)
