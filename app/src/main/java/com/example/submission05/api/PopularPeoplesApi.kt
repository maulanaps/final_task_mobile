package com.example.submission05.api

import com.example.submission05.model.Movies
import com.example.submission05.model.PopularPeoples
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface PopularPeoplesApi {
    @GET("person/popular?api_key=2a35719cd2c1e31362c38eeda7f0e117&page=1")
    fun getPopularPeoples() : Call<PopularPeoples>
}