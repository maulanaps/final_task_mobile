package com.example.submission05.data.remote.api

import com.example.submission05.data.remote.model.PopularPeopleDetail
import com.example.submission05.data.remote.model.PopularPeoples
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path


interface PopularPeoplesApi {
    @GET("person/popular?api_key=2a35719cd2c1e31362c38eeda7f0e117&page=1")
    fun getPopularPeoples() : Call<PopularPeoples>

    @GET("person/{people_id}?api_key=2a35719cd2c1e31362c38eeda7f0e117&language=en-US")
    fun getPeopleDetail(@Path("people_id") peopleId: Int) : Call<PopularPeopleDetail>
}