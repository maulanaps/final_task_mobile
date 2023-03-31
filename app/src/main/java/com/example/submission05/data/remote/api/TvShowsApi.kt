package com.example.submission05.data.remote.api

import com.example.submission03.model.MovieAndTvShow
import com.example.submission05.data.remote.model.Movies
import com.example.submission05.data.remote.model.TvShows
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface TvShowsApi {
    @GET("tv/popular?api_key=2a35719cd2c1e31362c38eeda7f0e117&language=en-US&page=1&limit=1")
    fun getPopularTvShows() : Call<Movies>

    @GET("tv/top_rated?api_key=2a35719cd2c1e31362c38eeda7f0e117&language=en-US&page=1&limit=1")
    fun getTopRatedShows() : Call<Movies>

    @GET("tv/on_the_air?api_key=2a35719cd2c1e31362c38eeda7f0e117&language=en-US&page=1&limit=1")
    fun getOnTheAirTvShows() : Call<Movies>

    @GET("tv/airing_today?api_key=2a35719cd2c1e31362c38eeda7f0e117&language=en-US&page=1&limit=1")
    fun getAiringTodayTvShows() : Call<Movies>

    @GET("tv/{tvShow_id}?api_key=2a35719cd2c1e31362c38eeda7f0e117&language=en-US")
    fun getTvShowDetail(@Path("tvShow_id") tvShowId: Int) : Call<MovieAndTvShow>
}