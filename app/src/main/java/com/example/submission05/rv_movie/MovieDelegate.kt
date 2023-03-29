package com.example.submission03.movie

import com.example.submission03.model.MovieAndTvShow

interface MovieDelegate {
    fun onItemClicked(movieAndTvShow: MovieAndTvShow)
}