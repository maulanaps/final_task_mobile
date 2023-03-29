package com.example.submission05.api

import com.example.submission05.model.Movies
import com.example.submission05.model.TvShows
import retrofit2.Call
import retrofit2.http.GET

interface TvShowsApi {
    @GET("tv/popular?api_key=2a35719cd2c1e31362c38eeda7f0e117&language=en-US&page=1&limit=1")
    fun getPopularTvShows() : Call<Movies>

    @GET("tv/top_rated?api_key=2a35719cd2c1e31362c38eeda7f0e117&language=en-US&page=1&limit=1")
    fun getTopRatedShows() : Call<Movies>

    @GET("tv/on_the_air?api_key=2a35719cd2c1e31362c38eeda7f0e117&language=en-US&page=1&limit=1")
    fun getOnTheAirTvShows() : Call<Movies>

    @GET("tv/airing_today?api_key=2a35719cd2c1e31362c38eeda7f0e117&language=en-US&page=1&limit=1")
    fun getAiringTodayTvShows() : Call<Movies>
}