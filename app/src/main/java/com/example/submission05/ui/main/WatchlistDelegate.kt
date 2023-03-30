package com.example.submission05.ui.main

import com.example.submission03.model.MovieAndTvShow

interface WatchlistDelegate {
    fun onItemClicked(movieAndTvShow: MovieAndTvShow)
}