package com.example.submission05.rv_watchlist

import com.example.submission03.model.Movie

interface WatchlistDelegate {
    fun onItemClicked(movie: Movie)
}