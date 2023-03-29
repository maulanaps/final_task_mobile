package com.example.submission05.rv_watchlist

import com.example.submission03.model.MovieAndTvShow

interface WatchlistDelegate {
    fun onItemClicked(movieAndTvShow: MovieAndTvShow)
}