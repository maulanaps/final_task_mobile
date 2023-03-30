package com.example.submission05.ui.list_movie

import com.example.submission03.model.MovieAndTvShow

interface MovieDelegate {
    fun onItemClicked(movieAndTvShow: MovieAndTvShow)
}