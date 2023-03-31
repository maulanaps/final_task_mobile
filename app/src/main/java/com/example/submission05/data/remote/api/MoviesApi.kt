package com.example.submission05.data.remote.api

import com.example.submission03.model.MovieAndTvShow
import com.example.submission05.data.remote.model.Movies
import com.example.submission05.data.remote.model.PopularPeopleDetail
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MoviesApi {
    @GET("movie/top_rated?api_key=2a35719cd2c1e31362c38eeda7f0e117&language=en-US&page=1/")
    fun getTopRatedMovies() : Call<Movies>

    @GET("movie/upcoming?api_key=2a35719cd2c1e31362c38eeda7f0e117&language=en-US&page=1/")
    fun getUpcomingMovies() : Call<Movies>

    @GET("movie/now_playing?api_key=2a35719cd2c1e31362c38eeda7f0e117&language=en-US&page=1/")
    fun getNowPlayingMovies() : Call<Movies>

    @GET("movie/popular?api_key=2a35719cd2c1e31362c38eeda7f0e117&language=en-US&page=1/")
    fun getPopularMovies() : Call<Movies>

    @GET("search/movie?api_key=2a35719cd2c1e31362c38eeda7f0e117")
    fun searchMovies(@Query("query") query: String) : Call<Movies>

    @GET("movie/{movie_id}?api_key=2a35719cd2c1e31362c38eeda7f0e117&language=en-US")
    fun getMovieDetail(@Path("movie_id") movieId: Int) : Call<MovieAndTvShow>
}